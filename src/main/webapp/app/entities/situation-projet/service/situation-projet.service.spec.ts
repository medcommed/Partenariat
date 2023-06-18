import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { DATE_FORMAT } from 'app/config/input.constants';
import { ISituationProjet } from '../situation-projet.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../situation-projet.test-samples';

import { SituationProjetService, RestSituationProjet } from './situation-projet.service';

const requireRestSample: RestSituationProjet = {
  ...sampleWithRequiredData,
  dateStatutValid: sampleWithRequiredData.dateStatutValid?.format(DATE_FORMAT),
};

describe('SituationProjet Service', () => {
  let service: SituationProjetService;
  let httpMock: HttpTestingController;
  let expectedResult: ISituationProjet | ISituationProjet[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(SituationProjetService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should create a SituationProjet', () => {
      // eslint-disable-next-line @typescript-eslint/no-unused-vars
      const situationProjet = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(situationProjet).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a SituationProjet', () => {
      const situationProjet = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(situationProjet).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a SituationProjet', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of SituationProjet', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a SituationProjet', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addSituationProjetToCollectionIfMissing', () => {
      it('should add a SituationProjet to an empty array', () => {
        const situationProjet: ISituationProjet = sampleWithRequiredData;
        expectedResult = service.addSituationProjetToCollectionIfMissing([], situationProjet);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(situationProjet);
      });

      it('should not add a SituationProjet to an array that contains it', () => {
        const situationProjet: ISituationProjet = sampleWithRequiredData;
        const situationProjetCollection: ISituationProjet[] = [
          {
            ...situationProjet,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addSituationProjetToCollectionIfMissing(situationProjetCollection, situationProjet);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a SituationProjet to an array that doesn't contain it", () => {
        const situationProjet: ISituationProjet = sampleWithRequiredData;
        const situationProjetCollection: ISituationProjet[] = [sampleWithPartialData];
        expectedResult = service.addSituationProjetToCollectionIfMissing(situationProjetCollection, situationProjet);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(situationProjet);
      });

      it('should add only unique SituationProjet to an array', () => {
        const situationProjetArray: ISituationProjet[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const situationProjetCollection: ISituationProjet[] = [sampleWithRequiredData];
        expectedResult = service.addSituationProjetToCollectionIfMissing(situationProjetCollection, ...situationProjetArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const situationProjet: ISituationProjet = sampleWithRequiredData;
        const situationProjet2: ISituationProjet = sampleWithPartialData;
        expectedResult = service.addSituationProjetToCollectionIfMissing([], situationProjet, situationProjet2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(situationProjet);
        expect(expectedResult).toContain(situationProjet2);
      });

      it('should accept null and undefined values', () => {
        const situationProjet: ISituationProjet = sampleWithRequiredData;
        expectedResult = service.addSituationProjetToCollectionIfMissing([], null, situationProjet, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(situationProjet);
      });

      it('should return initial array if no SituationProjet is added', () => {
        const situationProjetCollection: ISituationProjet[] = [sampleWithRequiredData];
        expectedResult = service.addSituationProjetToCollectionIfMissing(situationProjetCollection, undefined, null);
        expect(expectedResult).toEqual(situationProjetCollection);
      });
    });

    describe('compareSituationProjet', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareSituationProjet(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareSituationProjet(entity1, entity2);
        const compareResult2 = service.compareSituationProjet(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareSituationProjet(entity1, entity2);
        const compareResult2 = service.compareSituationProjet(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareSituationProjet(entity1, entity2);
        const compareResult2 = service.compareSituationProjet(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
