import { IPartenaire, NewPartenaire } from './partenaire.model';

export const sampleWithRequiredData: IPartenaire = {
  id: 36133,
  nomPartenaire: 'Generic Corporate',
  tel: 'Real',
  email: 'Mac_Koss@gmail.com',
};

export const sampleWithPartialData: IPartenaire = {
  id: 40271,
  nomPartenaire: 'scale Directives',
  tel: 'Grocery',
  email: 'Lisandro.Deckow@gmail.com',
};

export const sampleWithFullData: IPartenaire = {
  id: 8388,
  nomPartenaire: 'withdrawal non-volatile',
  tel: 'invoice Account',
  email: 'Norene_Beahan46@yahoo.com',
};

export const sampleWithNewData: NewPartenaire = {
  nomPartenaire: 'Ball Global',
  tel: 'deposit Bedfordshire',
  email: 'Lulu_Waelchi97@gmail.com',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
