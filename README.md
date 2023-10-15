![made-with-java](https://raw.githubusercontent.com/forthebadge/for-the-badge/master/src/images/badges/made-with-java.svg)

[![powered-by-spring](https://img.shields.io/badge/Powered%20by-spring-blue?style=for-the-badge&logo=spring)](https://spring.io/)

![Platform](https://img.shields.io/badge/Platform-Linux|MacOS|Windows-1f425f.svg)

![Code coverage](https://img.shields.io/github/languages/top/thevickypedia/api-file-handler)
![License](https://img.shields.io/github/license/thevickypedia/api-file-handler)

# API File Handler
Upload and download files using Spring based API

## Env Variables
- **MAX_SIZE** - Maximum file size to be allowed.
- **UPLOADS** - Directory name to upload files into. _Creates a new one if not present already_
- **SOURCE** - Directory to download files from. _Creates a new one if not present already_
- **TOKEN** - Authentication token. _Required argument with `8` to `120` character limit_
- **PORT** - Port number to host the API. _Defaults to `8080`_
> Environment variables can be loaded from a `.env` file.

## Usage
### Health Status
```shell
curl --location 'http://localhost:8080/health' \
--header 'Authorization: MyToken'
```

### Upload File
**Fail on existing file**
```shell
curl --location 'http://localhost:8080/upload-file?deleteExisting=true' \
--header 'Authorization: MyToken' \
--form 'file=@"~/sample.txt"'
```
**Overwrite existing file**
```shell
curl --location 'http://localhost:8080/upload-file?deleteExisting=true' \
--header 'Authorization: MyToken' \
--form 'file=@"~/sample.txt"'
```

### Donwload File
```shell
curl --location 'http://localhost:8080/download-file?fileName=sample.docx' \
--header 'Authorization: MyToken'
```

## License & copyright
&copy; Vignesh Rao

Licensed under the [MIT License](https://github.com/thevickypedia/api-file-handler/blob/main/LICENSE)
