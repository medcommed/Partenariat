export interface IDomaineProjet {
  id: number;
  designationAr?: string | null;
  designationFr?: string | null;
}

export type NewDomaineProjet = Omit<IDomaineProjet, 'id'> & { id: null };
