import { ITypeConvention, NewTypeConvention } from './type-convention.model';

export const sampleWithRequiredData: ITypeConvention = {
  id: 53333,
  nomTypeAr: 'ubiquitous Ohio multi-byte',
};

export const sampleWithPartialData: ITypeConvention = {
  id: 7635,
  nomTypeAr: 'monetize',
  nomTypeFr: 'International payment',
};

export const sampleWithFullData: ITypeConvention = {
  id: 64986,
  nomTypeAr: 'Credit Intranet Gold',
  nomTypeFr: 'Kwacha',
};

export const sampleWithNewData: NewTypeConvention = {
  nomTypeAr: 'withdrawal Massachusetts',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
