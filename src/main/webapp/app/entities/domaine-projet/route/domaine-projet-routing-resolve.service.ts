import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IDomaineProjet } from '../domaine-projet.model';
import { DomaineProjetService } from '../service/domaine-projet.service';

@Injectable({ providedIn: 'root' })
export class DomaineProjetRoutingResolveService implements Resolve<IDomaineProjet | null> {
  constructor(protected service: DomaineProjetService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IDomaineProjet | null | never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((domaineProjet: HttpResponse<IDomaineProjet>) => {
          if (domaineProjet.body) {
            return of(domaineProjet.body);
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
