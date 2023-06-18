import { ICommune, NewCommune } from './commune.model';

export const sampleWithRequiredData: ICommune = {
  id: 22585,
  nomCommuneAr: 'Industrial bypassing',
};

export const sampleWithPartialData: ICommune = {
  id: 72981,
  nomCommuneAr: 'Consultant',
};

export const sampleWithFullData: ICommune = {
  id: 65094,
  nomCommuneAr: 'Assurance withdrawal',
  nomCommuneFr: 'fault-tolerant',
};

export const sampleWithNewData: NewCommune = {
  nomCommuneAr: 'pixel View Investor',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
