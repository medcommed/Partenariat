import dayjs from 'dayjs/esm';

import { ISituationProjet, NewSituationProjet } from './situation-projet.model';

export const sampleWithRequiredData: ISituationProjet = {
  id: 14478,
  dateStatutValid: dayjs('2023-06-17'),
  statutProjet: 'Cambridgeshire Refined Focused',
};

export const sampleWithPartialData: ISituationProjet = {
  id: 12581,
  dateStatutValid: dayjs('2023-06-17'),
  statutProjet: 'sensor',
};

export const sampleWithFullData: ISituationProjet = {
  id: 65715,
  dateStatutValid: dayjs('2023-06-18'),
  statutProjet: 'Operations',
};

export const sampleWithNewData: NewSituationProjet = {
  dateStatutValid: dayjs('2023-06-17'),
  statutProjet: 'index Concrete',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
