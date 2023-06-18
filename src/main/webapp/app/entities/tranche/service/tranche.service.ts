import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { DATE_FORMAT } from 'app/config/input.constants';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ITranche, NewTranche } from '../tranche.model';

export type PartialUpdateTranche = Partial<ITranche> & Pick<ITranche, 'id'>;

type RestOf<T extends ITranche | NewTranche> = Omit<T, 'dateDeffet'> & {
  dateDeffet?: string | null;
};

export type RestTranche = RestOf<ITranche>;

export type NewRestTranche = RestOf<NewTranche>;

export type PartialUpdateRestTranche = RestOf<PartialUpdateTranche>;

export type EntityResponseType = HttpResponse<ITranche>;
export type EntityArrayResponseType = HttpResponse<ITranche[]>;

@Injectable({ providedIn: 'root' })
export class TrancheService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/tranches');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(tranche: NewTranche): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(tranche);
    return this.http
      .post<RestTranche>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(tranche: ITranche): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(tranche);
    return this.http
      .put<RestTranche>(`${this.resourceUrl}/${this.getTrancheIdentifier(tranche)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(tranche: PartialUpdateTranche): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(tranche);
    return this.http
      .patch<RestTranche>(`${this.resourceUrl}/${this.getTrancheIdentifier(tranche)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestTranche>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestTranche[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getTrancheIdentifier(tranche: Pick<ITranche, 'id'>): number {
    return tranche.id;
  }

  compareTranche(o1: Pick<ITranche, 'id'> | null, o2: Pick<ITranche, 'id'> | null): boolean {
    return o1 && o2 ? this.getTrancheIdentifier(o1) === this.getTrancheIdentifier(o2) : o1 === o2;
  }

  addTrancheToCollectionIfMissing<Type extends Pick<ITranche, 'id'>>(
    trancheCollection: Type[],
    ...tranchesToCheck: (Type | null | undefined)[]
  ): Type[] {
    const tranches: Type[] = tranchesToCheck.filter(isPresent);
    if (tranches.length > 0) {
      const trancheCollectionIdentifiers = trancheCollection.map(trancheItem => this.getTrancheIdentifier(trancheItem)!);
      const tranchesToAdd = tranches.filter(trancheItem => {
        const trancheIdentifier = this.getTrancheIdentifier(trancheItem);
        if (trancheCollectionIdentifiers.includes(trancheIdentifier)) {
          return false;
        }
        trancheCollectionIdentifiers.push(trancheIdentifier);
        return true;
      });
      return [...tranchesToAdd, ...trancheCollection];
    }
    return trancheCollection;
  }

  protected convertDateFromClient<T extends ITranche | NewTranche | PartialUpdateTranche>(tranche: T): RestOf<T> {
    return {
      ...tranche,
      dateDeffet: tranche.dateDeffet?.format(DATE_FORMAT) ?? null,
    };
  }

  protected convertDateFromServer(restTranche: RestTranche): ITranche {
    return {
      ...restTranche,
      dateDeffet: restTranche.dateDeffet ? dayjs(restTranche.dateDeffet) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestTranche>): HttpResponse<ITranche> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestTranche[]>): HttpResponse<ITranche[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
