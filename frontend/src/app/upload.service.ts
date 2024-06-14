import { HttpClient } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { firstValueFrom } from "rxjs";
import { UploadResult } from "./models/upload-result.model";

@Injectable({
  providedIn: 'root'
})
export class UploadService {

  // TODO: Task 3.
  // You may add add parameters to the method

  photoData = ""
  constructor(private httpClient: HttpClient) { }

  upload(form: any, photo: Blob){
    const formData = new FormData();
    formData.set("title", form['title']);
    formData.set("comments", form['comments']);
    formData.set("picture", photo);
    
    return firstValueFrom(this.httpClient.post<UploadResult>('/api/image/upload', formData));
  }
}
