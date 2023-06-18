import { IConvention } from 'app/entities/convention/convention.model';

export interface IPartenaire {
  id: number;
  nomPartenaire?: string | null;
  tel?: string | null;
  email?: string | null;
  conventions?: Pick<IConvention, 'id'>[] | null;
}

export type NewPartenaire = Omit<IPartenaire, 'id'> & { id: null };
