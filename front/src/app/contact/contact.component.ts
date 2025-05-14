import { Component, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { InputTextModule } from 'primeng/inputtext';
import { InputTextareaModule } from 'primeng/inputtextarea';
import { ButtonModule } from 'primeng/button';
import { MessagesModule } from 'primeng/messages';
import { Message } from 'primeng/api';

@Component({
  selector: 'app-contact',
  standalone: true,
  imports: [
    CommonModule,
    ReactiveFormsModule,
    InputTextModule,
    InputTextareaModule,
    ButtonModule,
    MessagesModule
  ],
  templateUrl: './contact.component.html',
  styleUrls: ['./contact.component.css'] // Corrected from scss to css as generated
})
export class ContactComponent {
  private fb = inject(FormBuilder);
  messages: Message[] = [];

  contactForm = this.fb.group({
    email: ['', [Validators.required, Validators.email]],
    message: ['', [Validators.required, Validators.maxLength(300)]]
  });

  onSubmit() {
    if (this.contactForm.valid) {
      // Simulate sending data
      console.log('Form Submitted!', this.contactForm.value);
      this.messages = [{ severity: 'success', summary: 'Succès', detail: 'Demande de contact envoyée avec succès.' }];
      this.contactForm.reset();
      // Clear messages after a delay
      setTimeout(() => this.messages = [], 5000);
    } else {
      // Mark fields as touched to show errors
      this.contactForm.markAllAsTouched();
    }
  }

  // Helper getters for template validation access
  get email() { return this.contactForm.get('email'); }
  get message() { return this.contactForm.get('message'); }
}
