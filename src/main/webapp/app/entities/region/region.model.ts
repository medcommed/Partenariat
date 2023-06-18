export interface IRegion {
  id: number;
  libelleRegionAr?: string | null;
  libelleRegionFr?: string | null;
}

export type NewRegion = Omit<IRegion, 'id'> & { id: null };
