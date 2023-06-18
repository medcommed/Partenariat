import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ITypeConvention } from '../type-convention.model';
import { TypeConventionService } from '../service/type-convention.service';

@Injectable({ providedIn: 'root' })
export class TypeConventionRoutingResolveService implements Resolve<ITypeConvention | null> {
  constructor(protected service: TypeConventionService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ITypeConvention | null | never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((typeConvention: HttpResponse<ITypeConvention>) => {
          if (typeConvention.body) {
            return of(typeConvention.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(null);
  }
}
