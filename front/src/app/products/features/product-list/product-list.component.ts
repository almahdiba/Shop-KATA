import { Component, OnInit, inject, signal, computed } from "@angular/core";
import { DatePipe, NgClass, NgIf, CurrencyPipe, NgFor } from "@angular/common";
import { Product } from "app/products/data-access/product.model";
import { ProductsService } from "app/products/data-access/products.service";
import { CartService } from "app/products/data-access/cart.service";
import { ProductFormComponent } from "app/products/ui/product-form/product-form.component";
import { ButtonModule } from "primeng/button";
import { CardModule } from "primeng/card";
import { DataViewModule } from 'primeng/dataview';
import { DialogModule } from 'primeng/dialog';
import { ToastModule } from 'primeng/toast';
import { MessageService } from 'primeng/api';
import { PaginatorModule, PaginatorState } from 'primeng/paginator';

const emptyProduct: Product = {
  id: 0,
  code: "",
  name: "",
  description: "",
  image: "",
  category: "",
  price: 0,
  quantity: 0,
  internalReference: "",
  shellId: 0,
  inventoryStatus: "INSTOCK",
  rating: 0,
  createdAt: 0,
  updatedAt: 0,
};

@Component({
  selector: "app-product-list",
  templateUrl: "./product-list.component.html",
  styleUrls: ["./product-list.component.scss"],
  standalone: true,
  imports: [
    DataViewModule, 
    CardModule, 
    ButtonModule, 
    DialogModule, 
    ProductFormComponent, 
    NgIf, 
    NgClass, 
    DatePipe,
    CurrencyPipe,
    NgFor,
    ToastModule,
    PaginatorModule
  ],
  providers: [MessageService]
})
export class ProductListComponent implements OnInit {
  private readonly productsService = inject(ProductsService);
  private readonly cartService = inject(CartService);
  private readonly messageService = inject(MessageService);

  public readonly allProducts = this.productsService.products;
  public paginatedProducts = computed(() => {
    const products = this.allProducts();
    const start = this.first();
    const end = this.first() + this.rows();
    return products.slice(start, end);
  });

  public isDialogVisible = false;
  public isCreation = false;
  public readonly editedProduct = signal<Product>(emptyProduct);

  public first = signal(0);
  public rows = signal(5); // Number of products per page
  public totalRecords = computed(() => this.allProducts().length);


  ngOnInit() {
    this.productsService.get().subscribe();
  }

  onPageChange(event: PaginatorState) {
    this.first.set(event.first || 0);
    this.rows.set(event.rows || 5);
  }

  public getSeverity(product: Product): string {
    switch (product.inventoryStatus) {
      case 'INSTOCK':
        return 'success';
      case 'LOWSTOCK':
        return 'warning';
      case 'OUTOFSTOCK':
        return 'danger';
      default:
        return 'info';
    }
  }

  public getInventoryStatusLabel(status: string): string {
    const statusMap: {[key: string]: string} = {
      'INSTOCK': 'En stock',
      'LOWSTOCK': 'Stock bas',
      'OUTOFSTOCK': 'Rupture de stock'
    };
    
    return statusMap[status] || status;
  }

  public addToCart(product: Product): void {
    this.cartService.addToCart(product);
    this.messageService.add({
      severity: 'success',
      summary: 'Ajouté au panier',
      detail: `${product.name} a été ajouté au panier`
    });
  }

  public onCreate() {
    this.isCreation = true;
    this.isDialogVisible = true;
    this.editedProduct.set(emptyProduct);
  }

  public onUpdate(product: Product) {
    this.isCreation = false;
    this.isDialogVisible = true;
    this.editedProduct.set(product);
  }

  public onDelete(product: Product) {
    this.productsService.delete(product.id).subscribe();
  }

  public onSave(product: Product) {
    if (this.isCreation) {
      this.productsService.create(product).subscribe();
    } else {
      this.productsService.update(product).subscribe();
    }
    this.closeDialog();
  }

  public onCancel() {
    this.closeDialog();
  }

  private closeDialog() {
    this.isDialogVisible = false;
  }
}
