import dayjs from 'dayjs/esm';

import { IProjet, NewProjet } from './projet.model';

export const sampleWithRequiredData: IProjet = {
  id: 59803,
  nomProjet: 'Communications interface',
  anneeProjet: 'Generic Future',
  dateDebut: dayjs('2023-06-17'),
  dureeProjet: 'Refined PCI Kids',
  montantProjet: 19102,
};

export const sampleWithPartialData: IProjet = {
  id: 25292,
  nomProjet: 'Account Concrete Forward',
  anneeProjet: 'deposit bandwidth',
  dateDebut: dayjs('2023-06-17'),
  dureeProjet: 'parsing Berkshire cultivate',
  montantProjet: 67160,
};

export const sampleWithFullData: IProjet = {
  id: 20615,
  nomProjet: 'regional PCI orchid',
  anneeProjet: 'copying synthesizing Integration',
  dateDebut: dayjs('2023-06-17'),
  dureeProjet: 'Loan Salad Serbia',
  montantProjet: 47033,
};

export const sampleWithNewData: NewProjet = {
  nomProjet: 'Planner Realigned quantify',
  anneeProjet: 'Steel Washington program',
  dateDebut: dayjs('2023-06-17'),
  dureeProjet: 'streamline',
  montantProjet: 96063,
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
