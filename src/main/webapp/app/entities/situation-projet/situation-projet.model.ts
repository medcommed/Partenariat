import dayjs from 'dayjs/esm';
import { IProjet } from 'app/entities/projet/projet.model';

export interface ISituationProjet {
  id: number;
  dateStatutValid?: dayjs.Dayjs | null;
  statutProjet?: string | null;
  projet?: Pick<IProjet, 'id'> | null;
}

export type NewSituationProjet = Omit<ISituationProjet, 'id'> & { id: null };
