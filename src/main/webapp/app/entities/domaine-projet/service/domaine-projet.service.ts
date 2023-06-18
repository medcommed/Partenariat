import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IDomaineProjet, NewDomaineProjet } from '../domaine-projet.model';

export type PartialUpdateDomaineProjet = Partial<IDomaineProjet> & Pick<IDomaineProjet, 'id'>;

export type EntityResponseType = HttpResponse<IDomaineProjet>;
export type EntityArrayResponseType = HttpResponse<IDomaineProjet[]>;

@Injectable({ providedIn: 'root' })
export class DomaineProjetService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/domaine-projets');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(domaineProjet: NewDomaineProjet): Observable<EntityResponseType> {
    return this.http.post<IDomaineProjet>(this.resourceUrl, domaineProjet, { observe: 'response' });
  }

  update(domaineProjet: IDomaineProjet): Observable<EntityResponseType> {
    return this.http.put<IDomaineProjet>(`${this.resourceUrl}/${this.getDomaineProjetIdentifier(domaineProjet)}`, domaineProjet, {
      observe: 'response',
    });
  }

  partialUpdate(domaineProjet: PartialUpdateDomaineProjet): Observable<EntityResponseType> {
    return this.http.patch<IDomaineProjet>(`${this.resourceUrl}/${this.getDomaineProjetIdentifier(domaineProjet)}`, domaineProjet, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IDomaineProjet>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IDomaineProjet[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getDomaineProjetIdentifier(domaineProjet: Pick<IDomaineProjet, 'id'>): number {
    return domaineProjet.id;
  }

  compareDomaineProjet(o1: Pick<IDomaineProjet, 'id'> | null, o2: Pick<IDomaineProjet, 'id'> | null): boolean {
    return o1 && o2 ? this.getDomaineProjetIdentifier(o1) === this.getDomaineProjetIdentifier(o2) : o1 === o2;
  }

  addDomaineProjetToCollectionIfMissing<Type extends Pick<IDomaineProjet, 'id'>>(
    domaineProjetCollection: Type[],
    ...domaineProjetsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const domaineProjets: Type[] = domaineProjetsToCheck.filter(isPresent);
    if (domaineProjets.length > 0) {
      const domaineProjetCollectionIdentifiers = domaineProjetCollection.map(
        domaineProjetItem => this.getDomaineProjetIdentifier(domaineProjetItem)!
      );
      const domaineProjetsToAdd = domaineProjets.filter(domaineProjetItem => {
        const domaineProjetIdentifier = this.getDomaineProjetIdentifier(domaineProjetItem);
        if (domaineProjetCollectionIdentifiers.includes(domaineProjetIdentifier)) {
          return false;
        }
        domaineProjetCollectionIdentifiers.push(domaineProjetIdentifier);
        return true;
      });
      return [...domaineProjetsToAdd, ...domaineProjetCollection];
    }
    return domaineProjetCollection;
  }
}
