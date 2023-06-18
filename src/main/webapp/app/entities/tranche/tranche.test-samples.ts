import dayjs from 'dayjs/esm';

import { ITranche, NewTranche } from './tranche.model';

export const sampleWithRequiredData: ITranche = {
  id: 32211,
  nomTranche: 'GB Analyst',
  dateDeffet: dayjs('2023-06-18'),
  montantTranche: 69777,
  rapportTranche: 'maroon envisioneer circuit',
};

export const sampleWithPartialData: ITranche = {
  id: 58082,
  nomTranche: 'mint Director',
  dateDeffet: dayjs('2023-06-17'),
  montantTranche: 55290,
  rapportTranche: 'withdrawal Officer',
};

export const sampleWithFullData: ITranche = {
  id: 9995,
  nomTranche: 'deposit',
  dateDeffet: dayjs(undefined),
  montantTranche: 79563,
  rapportTranche: 'Euro',
};

export const sampleWithNewData: NewTranche = {
  nomTranche: 'plug-and-play',
  dateDeffet: dayjs(undefined),
  montantTranche: 83162,
  rapportTranche: '1080p',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
