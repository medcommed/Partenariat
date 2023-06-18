import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { ICommune } from '../commune.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../commune.test-samples';

import { CommuneService } from './commune.service';

const requireRestSample: ICommune = {
  ...sampleWithRequiredData,
};

describe('Commune Service', () => {
  let service: CommuneService;
  let httpMock: HttpTestingController;
  let expectedResult: ICommune | ICommune[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(CommuneService);
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

    it('should create a Commune', () => {
      // eslint-disable-next-line @typescript-eslint/no-unused-vars
      const commune = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(commune).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Commune', () => {
      const commune = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(commune).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Commune', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Commune', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a Commune', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addCommuneToCollectionIfMissing', () => {
      it('should add a Commune to an empty array', () => {
        const commune: ICommune = sampleWithRequiredData;
        expectedResult = service.addCommuneToCollectionIfMissing([], commune);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(commune);
      });

      it('should not add a Commune to an array that contains it', () => {
        const commune: ICommune = sampleWithRequiredData;
        const communeCollection: ICommune[] = [
          {
            ...commune,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addCommuneToCollectionIfMissing(communeCollection, commune);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Commune to an array that doesn't contain it", () => {
        const commune: ICommune = sampleWithRequiredData;
        const communeCollection: ICommune[] = [sampleWithPartialData];
        expectedResult = service.addCommuneToCollectionIfMissing(communeCollection, commune);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(commune);
      });

      it('should add only unique Commune to an array', () => {
        const communeArray: ICommune[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const communeCollection: ICommune[] = [sampleWithRequiredData];
        expectedResult = service.addCommuneToCollectionIfMissing(communeCollection, ...communeArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const commune: ICommune = sampleWithRequiredData;
        const commune2: ICommune = sampleWithPartialData;
        expectedResult = service.addCommuneToCollectionIfMissing([], commune, commune2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(commune);
        expect(expectedResult).toContain(commune2);
      });

      it('should accept null and undefined values', () => {
        const commune: ICommune = sampleWithRequiredData;
        expectedResult = service.addCommuneToCollectionIfMissing([], null, commune, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(commune);
      });

      it('should return initial array if no Commune is added', () => {
        const communeCollection: ICommune[] = [sampleWithRequiredData];
        expectedResult = service.addCommuneToCollectionIfMissing(communeCollection, undefined, null);
        expect(expectedResult).toEqual(communeCollection);
      });
    });

    describe('compareCommune', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareCommune(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareCommune(entity1, entity2);
        const compareResult2 = service.compareCommune(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareCommune(entity1, entity2);
        const compareResult2 = service.compareCommune(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareCommune(entity1, entity2);
        const compareResult2 = service.compareCommune(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
