import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { DATE_FORMAT } from 'app/config/input.constants';
import { ITranche } from '../tranche.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../tranche.test-samples';

import { TrancheService, RestTranche } from './tranche.service';

const requireRestSample: RestTranche = {
  ...sampleWithRequiredData,
  dateDeffet: sampleWithRequiredData.dateDeffet?.format(DATE_FORMAT),
};

describe('Tranche Service', () => {
  let service: TrancheService;
  let httpMock: HttpTestingController;
  let expectedResult: ITranche | ITranche[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(TrancheService);
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

    it('should create a Tranche', () => {
      // eslint-disable-next-line @typescript-eslint/no-unused-vars
      const tranche = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(tranche).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Tranche', () => {
      const tranche = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(tranche).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Tranche', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Tranche', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a Tranche', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addTrancheToCollectionIfMissing', () => {
      it('should add a Tranche to an empty array', () => {
        const tranche: ITranche = sampleWithRequiredData;
        expectedResult = service.addTrancheToCollectionIfMissing([], tranche);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(tranche);
      });

      it('should not add a Tranche to an array that contains it', () => {
        const tranche: ITranche = sampleWithRequiredData;
        const trancheCollection: ITranche[] = [
          {
            ...tranche,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addTrancheToCollectionIfMissing(trancheCollection, tranche);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Tranche to an array that doesn't contain it", () => {
        const tranche: ITranche = sampleWithRequiredData;
        const trancheCollection: ITranche[] = [sampleWithPartialData];
        expectedResult = service.addTrancheToCollectionIfMissing(trancheCollection, tranche);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(tranche);
      });

      it('should add only unique Tranche to an array', () => {
        const trancheArray: ITranche[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const trancheCollection: ITranche[] = [sampleWithRequiredData];
        expectedResult = service.addTrancheToCollectionIfMissing(trancheCollection, ...trancheArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const tranche: ITranche = sampleWithRequiredData;
        const tranche2: ITranche = sampleWithPartialData;
        expectedResult = service.addTrancheToCollectionIfMissing([], tranche, tranche2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(tranche);
        expect(expectedResult).toContain(tranche2);
      });

      it('should accept null and undefined values', () => {
        const tranche: ITranche = sampleWithRequiredData;
        expectedResult = service.addTrancheToCollectionIfMissing([], null, tranche, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(tranche);
      });

      it('should return initial array if no Tranche is added', () => {
        const trancheCollection: ITranche[] = [sampleWithRequiredData];
        expectedResult = service.addTrancheToCollectionIfMissing(trancheCollection, undefined, null);
        expect(expectedResult).toEqual(trancheCollection);
      });
    });

    describe('compareTranche', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareTranche(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareTranche(entity1, entity2);
        const compareResult2 = service.compareTranche(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareTranche(entity1, entity2);
        const compareResult2 = service.compareTranche(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareTranche(entity1, entity2);
        const compareResult2 = service.compareTranche(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
