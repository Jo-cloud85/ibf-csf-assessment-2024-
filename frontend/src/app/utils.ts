// IMPORTANT: Do not modify this file

// Code from https://medium.com/@impulsejs/convert-dataurl-to-a-file-in-javascript-1921b8c3f4b
export const dataToImage = (dataUrl: string, filename = 'pic.png') => {
  var arr = dataUrl.split(",")
  // @ts-ignore
  const mime = arr[0].match(/:(.*?);/)[1]
  const  bstr = atob(arr[arr.length - 1])
  var n = bstr.length
  // @ts-ignore
  const u8arr = new Uint8Array(n);
  while (n--) {
    u8arr[n] = bstr.charCodeAt(n);
  }
  return new File([u8arr], filename, { type: mime });
}
