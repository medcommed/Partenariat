import { IRegion } from 'app/entities/region/region.model';

export interface IProvince {
  id: number;
  libelleProvinceAr?: string | null;
  libelleProvinceFr?: string | null;
  region?: Pick<IRegion, 'id' | 'libelleRegionAr'> | null;
}

export type NewProvince = Omit<IProvince, 'id'> & { id: null };
