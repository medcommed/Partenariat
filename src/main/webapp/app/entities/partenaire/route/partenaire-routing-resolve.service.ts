import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IPartenaire } from '../partenaire.model';
import { PartenaireService } from '../service/partenaire.service';

@Injectable({ providedIn: 'root' })
export class PartenaireRoutingResolveService implements Resolve<IPartenaire | null> {
  constructor(protected service: PartenaireService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IPartenaire | null | never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((partenaire: HttpResponse<IPartenaire>) => {
          if (partenaire.body) {
            return of(partenaire.body);
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
