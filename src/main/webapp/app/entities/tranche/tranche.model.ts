import dayjs from 'dayjs/esm';
import { IProjet } from 'app/entities/projet/projet.model';

export interface ITranche {
  id: number;
  nomTranche?: string | null;
  dateDeffet?: dayjs.Dayjs | null;
  montantTranche?: number | null;
  rapportTranche?: string | null;
  projet?: Pick<IProjet, 'id'> | null;
}

export type NewTranche = Omit<ITranche, 'id'> & { id: null };
