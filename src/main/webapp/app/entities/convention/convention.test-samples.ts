import dayjs from 'dayjs/esm';

import { IConvention, NewConvention } from './convention.model';

export const sampleWithRequiredData: IConvention = {
  id: 93007,
  aveanau: 'ADP Buckinghamshire',
  dateDebutConv: dayjs('2023-06-17'),
  dureeConvention: 'Cambridgeshire',
  etatConvention: 'payment',
  nbrTranche: 8645,
  nomConvention: 'protocol',
  objectif: '../fake-data/blob/hipster.txt',
};

export const sampleWithPartialData: IConvention = {
  id: 36079,
  aveanau: 'generating',
  dateDebutConv: dayjs('2023-06-18'),
  dureeConvention: 'Plastic',
  etatConvention: 'calculating invoice COM',
  nbrTranche: 21531,
  nomConvention: 'Shirt Reduced auxiliary',
  objectif: '../fake-data/blob/hipster.txt',
};

export const sampleWithFullData: IConvention = {
  id: 74025,
  aveanau: 'Fantastic Borders digital',
  dateDebutConv: dayjs('2023-06-17'),
  dureeConvention: 'morph Cambridgeshire Island',
  etatConvention: 'visionary Berkshire',
  nbrTranche: 48698,
  nomConvention: 'extensible Plastic',
  objectif: '../fake-data/blob/hipster.txt',
};

export const sampleWithNewData: NewConvention = {
  aveanau: 'quantify',
  dateDebutConv: dayjs('2023-06-17'),
  dureeConvention: 'Account Assistant',
  etatConvention: 'system bandwidth',
  nbrTranche: 11853,
  nomConvention: 'cross-platform Steel calculating',
  objectif: '../fake-data/blob/hipster.txt',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
