import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ITypeConvention, NewTypeConvention } from '../type-convention.model';

export type PartialUpdateTypeConvention = Partial<ITypeConvention> & Pick<ITypeConvention, 'id'>;

export type EntityResponseType = HttpResponse<ITypeConvention>;
export type EntityArrayResponseType = HttpResponse<ITypeConvention[]>;

@Injectable({ providedIn: 'root' })
export class TypeConventionService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/type-conventions');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(typeConvention: NewTypeConvention): Observable<EntityResponseType> {
    return this.http.post<ITypeConvention>(this.resourceUrl, typeConvention, { observe: 'response' });
  }

  update(typeConvention: ITypeConvention): Observable<EntityResponseType> {
    return this.http.put<ITypeConvention>(`${this.resourceUrl}/${this.getTypeConventionIdentifier(typeConvention)}`, typeConvention, {
      observe: 'response',
    });
  }

  partialUpdate(typeConvention: PartialUpdateTypeConvention): Observable<EntityResponseType> {
    return this.http.patch<ITypeConvention>(`${this.resourceUrl}/${this.getTypeConventionIdentifier(typeConvention)}`, typeConvention, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<ITypeConvention>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ITypeConvention[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getTypeConventionIdentifier(typeConvention: Pick<ITypeConvention, 'id'>): number {
    return typeConvention.id;
  }

  compareTypeConvention(o1: Pick<ITypeConvention, 'id'> | null, o2: Pick<ITypeConvention, 'id'> | null): boolean {
    return o1 && o2 ? this.getTypeConventionIdentifier(o1) === this.getTypeConventionIdentifier(o2) : o1 === o2;
  }

  addTypeConventionToCollectionIfMissing<Type extends Pick<ITypeConvention, 'id'>>(
    typeConventionCollection: Type[],
    ...typeConventionsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const typeConventions: Type[] = typeConventionsToCheck.filter(isPresent);
    if (typeConventions.length > 0) {
      const typeConventionCollectionIdentifiers = typeConventionCollection.map(
        typeConventionItem => this.getTypeConventionIdentifier(typeConventionItem)!
      );
      const typeConventionsToAdd = typeConventions.filter(typeConventionItem => {
        const typeConventionIdentifier = this.getTypeConventionIdentifier(typeConventionItem);
        if (typeConventionCollectionIdentifiers.includes(typeConventionIdentifier)) {
          return false;
        }
        typeConventionCollectionIdentifiers.push(typeConventionIdentifier);
        return true;
      });
      return [...typeConventionsToAdd, ...typeConventionCollection];
    }
    return typeConventionCollection;
  }
}
