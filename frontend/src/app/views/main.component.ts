import { Component, OnChanges, OnDestroy, OnInit, ViewChild, inject } from '@angular/core';
import { Router } from '@angular/router';
import { WebcamComponent, WebcamImage } from 'ngx-webcam';
import { Subject, Subscription } from 'rxjs';
import { UploadService } from '../upload.service';

@Component({
  selector: 'app-main',
  templateUrl: './main.component.html',
  styleUrl: './main.component.css'
})
export class MainComponent implements OnInit, OnDestroy {

  // TODO: Task 1
  @ViewChild(WebcamComponent)
  webcam!: WebcamComponent;
  width =  500;
  height = 375;
  pics: string[] = []
  sub$!: Subscription;
  trigger = new Subject<void>;
  imageType: string = 'image/png';

  // aspectGroup !: number;

  // const aspectHeight[] = [[500, 282]];

  private readonly router = inject(Router);
  private readonly uploadSvc = inject(UploadService);

  ngOnInit(): void {
      console.log("init ... " + this.webcam);
  }

  // onFileChange(event: any): void {
  //   const file = event.target.files[0];
  //   if (file) {
  //     this.empForm.patchValue({ fileSource: file});
  //     const reader = new FileReader();
  //     reader.onload = () => {
  //       this.filePreview = reader.result;
  //     };
  //     reader.readAsDataURL(file);
  //   }
  // }

  ngAfterViewInit(): void {
    this.webcam.trigger = this.trigger;
    this.sub$ = this.webcam.imageCapture.subscribe(
      this.takePic.bind(this)
    )
  }
  
  // ngOnChanges(): void {
  //   this.height = this.aspectGroup;
  //   this.webcam.trigger = this.trigger;
  // }

  aspect1() {
    this.height = 282;
    this.width = 500;
    this.webcam.trigger = this.trigger;
  }

  aspect2() {
    this.height = 375;
    this.width = 500;
    this.webcam.trigger = this.trigger;
  }

  aspect3() {
    this.height = 333;
    this.width = 500;
    this.webcam.trigger = this.trigger;
  }

  aspect4() {
    this.height = 500;
    this.width = 500;
    this.webcam.trigger = this.trigger;
  }

  snap(){
    this.trigger.next();
    this.router.navigate(['/upload-picture'])
  }

  takePic(webcamImg: WebcamImage){
    this.uploadSvc.photoData = webcamImg.imageAsDataUrl;
    this.pics.push(webcamImg.imageAsDataUrl);
  }

  ngOnDestroy(): void {
    this.sub$.unsubscribe();
  }
}
