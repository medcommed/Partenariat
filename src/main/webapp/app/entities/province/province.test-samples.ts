import { IProvince, NewProvince } from './province.model';

export const sampleWithRequiredData: IProvince = {
  id: 16991,
  libelleProvinceAr: 'models repurpose',
};

export const sampleWithPartialData: IProvince = {
  id: 77656,
  libelleProvinceAr: 'EXE Administrator value-added',
};

export const sampleWithFullData: IProvince = {
  id: 73242,
  libelleProvinceAr: 'ivory',
  libelleProvinceFr: 'Mountain',
};

export const sampleWithNewData: NewProvince = {
  libelleProvinceAr: 'Fantastic',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
