import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { DATE_FORMAT } from 'app/config/input.constants';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ISituationProjet, NewSituationProjet } from '../situation-projet.model';

export type PartialUpdateSituationProjet = Partial<ISituationProjet> & Pick<ISituationProjet, 'id'>;

type RestOf<T extends ISituationProjet | NewSituationProjet> = Omit<T, 'dateStatutValid'> & {
  dateStatutValid?: string | null;
};

export type RestSituationProjet = RestOf<ISituationProjet>;

export type NewRestSituationProjet = RestOf<NewSituationProjet>;

export type PartialUpdateRestSituationProjet = RestOf<PartialUpdateSituationProjet>;

export type EntityResponseType = HttpResponse<ISituationProjet>;
export type EntityArrayResponseType = HttpResponse<ISituationProjet[]>;

@Injectable({ providedIn: 'root' })
export class SituationProjetService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/situation-projets');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(situationProjet: NewSituationProjet): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(situationProjet);
    return this.http
      .post<RestSituationProjet>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(situationProjet: ISituationProjet): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(situationProjet);
    return this.http
      .put<RestSituationProjet>(`${this.resourceUrl}/${this.getSituationProjetIdentifier(situationProjet)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(situationProjet: PartialUpdateSituationProjet): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(situationProjet);
    return this.http
      .patch<RestSituationProjet>(`${this.resourceUrl}/${this.getSituationProjetIdentifier(situationProjet)}`, copy, {
        observe: 'response',
      })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestSituationProjet>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestSituationProjet[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getSituationProjetIdentifier(situationProjet: Pick<ISituationProjet, 'id'>): number {
    return situationProjet.id;
  }

  compareSituationProjet(o1: Pick<ISituationProjet, 'id'> | null, o2: Pick<ISituationProjet, 'id'> | null): boolean {
    return o1 && o2 ? this.getSituationProjetIdentifier(o1) === this.getSituationProjetIdentifier(o2) : o1 === o2;
  }

  addSituationProjetToCollectionIfMissing<Type extends Pick<ISituationProjet, 'id'>>(
    situationProjetCollection: Type[],
    ...situationProjetsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const situationProjets: Type[] = situationProjetsToCheck.filter(isPresent);
    if (situationProjets.length > 0) {
      const situationProjetCollectionIdentifiers = situationProjetCollection.map(
        situationProjetItem => this.getSituationProjetIdentifier(situationProjetItem)!
      );
      const situationProjetsToAdd = situationProjets.filter(situationProjetItem => {
        const situationProjetIdentifier = this.getSituationProjetIdentifier(situationProjetItem);
        if (situationProjetCollectionIdentifiers.includes(situationProjetIdentifier)) {
          return false;
        }
        situationProjetCollectionIdentifiers.push(situationProjetIdentifier);
        return true;
      });
      return [...situationProjetsToAdd, ...situationProjetCollection];
    }
    return situationProjetCollection;
  }

  protected convertDateFromClient<T extends ISituationProjet | NewSituationProjet | PartialUpdateSituationProjet>(
    situationProjet: T
  ): RestOf<T> {
    return {
      ...situationProjet,
      dateStatutValid: situationProjet.dateStatutValid?.format(DATE_FORMAT) ?? null,
    };
  }

  protected convertDateFromServer(restSituationProjet: RestSituationProjet): ISituationProjet {
    return {
      ...restSituationProjet,
      dateStatutValid: restSituationProjet.dateStatutValid ? dayjs(restSituationProjet.dateStatutValid) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestSituationProjet>): HttpResponse<ISituationProjet> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestSituationProjet[]>): HttpResponse<ISituationProjet[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
