export interface ITypeConvention {
  id: number;
  nomTypeAr?: string | null;
  nomTypeFr?: string | null;
}

export type NewTypeConvention = Omit<ITypeConvention, 'id'> & { id: null };
