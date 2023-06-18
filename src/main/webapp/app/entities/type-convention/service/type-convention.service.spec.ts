import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { ITypeConvention } from '../type-convention.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../type-convention.test-samples';

import { TypeConventionService } from './type-convention.service';

const requireRestSample: ITypeConvention = {
  ...sampleWithRequiredData,
};

describe('TypeConvention Service', () => {
  let service: TypeConventionService;
  let httpMock: HttpTestingController;
  let expectedResult: ITypeConvention | ITypeConvention[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(TypeConventionService);
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

    it('should create a TypeConvention', () => {
      // eslint-disable-next-line @typescript-eslint/no-unused-vars
      const typeConvention = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(typeConvention).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a TypeConvention', () => {
      const typeConvention = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(typeConvention).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a TypeConvention', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of TypeConvention', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a TypeConvention', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addTypeConventionToCollectionIfMissing', () => {
      it('should add a TypeConvention to an empty array', () => {
        const typeConvention: ITypeConvention = sampleWithRequiredData;
        expectedResult = service.addTypeConventionToCollectionIfMissing([], typeConvention);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(typeConvention);
      });

      it('should not add a TypeConvention to an array that contains it', () => {
        const typeConvention: ITypeConvention = sampleWithRequiredData;
        const typeConventionCollection: ITypeConvention[] = [
          {
            ...typeConvention,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addTypeConventionToCollectionIfMissing(typeConventionCollection, typeConvention);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a TypeConvention to an array that doesn't contain it", () => {
        const typeConvention: ITypeConvention = sampleWithRequiredData;
        const typeConventionCollection: ITypeConvention[] = [sampleWithPartialData];
        expectedResult = service.addTypeConventionToCollectionIfMissing(typeConventionCollection, typeConvention);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(typeConvention);
      });

      it('should add only unique TypeConvention to an array', () => {
        const typeConventionArray: ITypeConvention[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const typeConventionCollection: ITypeConvention[] = [sampleWithRequiredData];
        expectedResult = service.addTypeConventionToCollectionIfMissing(typeConventionCollection, ...typeConventionArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const typeConvention: ITypeConvention = sampleWithRequiredData;
        const typeConvention2: ITypeConvention = sampleWithPartialData;
        expectedResult = service.addTypeConventionToCollectionIfMissing([], typeConvention, typeConvention2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(typeConvention);
        expect(expectedResult).toContain(typeConvention2);
      });

      it('should accept null and undefined values', () => {
        const typeConvention: ITypeConvention = sampleWithRequiredData;
        expectedResult = service.addTypeConventionToCollectionIfMissing([], null, typeConvention, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(typeConvention);
      });

      it('should return initial array if no TypeConvention is added', () => {
        const typeConventionCollection: ITypeConvention[] = [sampleWithRequiredData];
        expectedResult = service.addTypeConventionToCollectionIfMissing(typeConventionCollection, undefined, null);
        expect(expectedResult).toEqual(typeConventionCollection);
      });
    });

    describe('compareTypeConvention', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareTypeConvention(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareTypeConvention(entity1, entity2);
        const compareResult2 = service.compareTypeConvention(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareTypeConvention(entity1, entity2);
        const compareResult2 = service.compareTypeConvention(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareTypeConvention(entity1, entity2);
        const compareResult2 = service.compareTypeConvention(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
