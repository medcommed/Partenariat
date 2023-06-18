import dayjs from 'dayjs/esm';
import { ICommune } from 'app/entities/commune/commune.model';
import { IDomaineProjet } from 'app/entities/domaine-projet/domaine-projet.model';

export interface IProjet {
  id: number;
  nomProjet?: string | null;
  anneeProjet?: string | null;
  dateDebut?: dayjs.Dayjs | null;
  dureeProjet?: string | null;
  montantProjet?: number | null;
  comune?: Pick<ICommune, 'id' | 'nomCommuneAr'> | null;
  domaineProjet?: Pick<IDomaineProjet, 'id' | 'designationAr'> | null;
}

export type NewProjet = Omit<IProjet, 'id'> & { id: null };
