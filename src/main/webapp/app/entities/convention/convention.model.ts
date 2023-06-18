import dayjs from 'dayjs/esm';
import { IProjet } from 'app/entities/projet/projet.model';
import { ITypeConvention } from 'app/entities/type-convention/type-convention.model';
import { IPartenaire } from 'app/entities/partenaire/partenaire.model';

export interface IConvention {
  id: number;
  aveanau?: string | null;
  dateDebutConv?: dayjs.Dayjs | null;
  dureeConvention?: string | null;
  etatConvention?: string | null;
  nbrTranche?: number | null;
  nomConvention?: string | null;
  objectif?: string | null;
  projet?: Pick<IProjet, 'id'> | null;
  typeConvention?: Pick<ITypeConvention, 'id'> | null;
  partenaires?: Pick<IPartenaire, 'id'>[] | null;
}

export type NewConvention = Omit<IConvention, 'id'> & { id: null };
