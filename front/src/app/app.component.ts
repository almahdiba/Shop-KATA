import {
  Component,
} from "@angular/core";
import { RouterModule } from "@angular/router";
import { SplitterModule } from 'primeng/splitter';
import { ToolbarModule } from 'primeng/toolbar';
import { ToastModule } from 'primeng/toast';
import { PanelMenuComponent } from "./shared/ui/panel-menu/panel-menu.component";
import { CartBadgeComponent } from "./products/features/cart-badge/cart-badge.component";

@Component({
  selector: "app-root",
  templateUrl: "./app.component.html",
  styleUrls: ["./app.component.scss"],
  standalone: true,
  imports: [
    RouterModule, 
    SplitterModule, 
    ToolbarModule, 
    PanelMenuComponent, 
    CartBadgeComponent, 
    ToastModule
  ],
})
export class AppComponent {
  title = "ALTEN SHOP";
}
