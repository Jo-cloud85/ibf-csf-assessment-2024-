import { Component, inject } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { UploadService } from '../upload.service';
import { dataToImage } from '../utils';
import { HttpErrorResponse } from '@angular/common/http';

@Component({
  selector: 'app-picture',
  templateUrl: './picture.component.html',
  styleUrl: './picture.component.css'
})
export class PictureComponent {

  // TODO: Task 2
  // TODO: Task 3

  photoData = ""
  uploadForm!: FormGroup;
  blob!: Blob;

  private readonly fb = inject(FormBuilder);
  private readonly uploadSvc = inject(UploadService);
  private readonly router = inject(Router);
  // private readonly dataToImage = inject(utils);

  ngOnInit(): void {
    if(!this.uploadSvc.photoData){
      this.router.navigate(['/'])
      return;
    }
    this.photoData = this.uploadSvc.photoData;
    this.uploadForm = this.fb.group({
        title: this.fb.control<string>('', [Validators.required, Validators.minLength(5)]),
        comments: this.fb.control<string>(''),
    });
    this.blob = dataToImage(this.photoData); // method from the given file - utils.ts 
    console.log("BLOB FROM PICTURE COMPONENT..." + this.blob);
  }

  upload(){
    const formValue = this.uploadForm.value;
    this.uploadSvc.upload(formValue, this.blob)
    .then((result: any) => {
      console.log("Result from picture.component... " + JSON.stringify(result));
      this.router.navigate(['/']);
    })
    .catch((error: HttpErrorResponse) => {
      alert(`Picture upload failed: ${error.message}`);
      console.error(error);
    })
  }

  // If form is dirty, you wont be able to leave
  isFormDirty() {
    return this.uploadForm.dirty;
  }
}
