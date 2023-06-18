import { IProvince } from 'app/entities/province/province.model';

export interface ICommune {
  id: number;
  nomCommuneAr?: string | null;
  nomCommuneFr?: string | null;
  provinces?: Pick<IProvince, 'id' | 'libelleProvinceAr'> | null;
}

export type NewCommune = Omit<ICommune, 'id'> & { id: null };
