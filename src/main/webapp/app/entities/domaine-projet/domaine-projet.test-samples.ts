import { IDomaineProjet, NewDomaineProjet } from './domaine-projet.model';

export const sampleWithRequiredData: IDomaineProjet = {
  id: 11874,
  designationAr: 'Planner',
};

export const sampleWithPartialData: IDomaineProjet = {
  id: 61361,
  designationAr: 'Refined cross-platform',
};

export const sampleWithFullData: IDomaineProjet = {
  id: 80823,
  designationAr: 'Representative',
  designationFr: 'Cambridgeshire',
};

export const sampleWithNewData: NewDomaineProjet = {
  designationAr: 'Incredible Refined Music',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
