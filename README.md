[![made-with-spring](https://img.shields.io/badge/Made%20with-Java-blue?style=for-the-badge&logo=spring)](https://spring.io/)

![Platform](https://img.shields.io/badge/Platform-Linux|MacOS|Windows-1f425f.svg)

![Code coverage](https://img.shields.io/github/languages/top/thevickypedia/api-file-handler)
![License](https://img.shields.io/github/license/thevickypedia/api-file-handler)

# API File Handler
Upload and download files using Spring based API

## Env Variables
- **MAX_SIZE** - Maximum file size to be allowed.
- **UPLOADS** - Directory name to upload files into. _Creates a new one if not present already_
- **SOURCE** - Directory to download files from. _Creates a new one if not present already_
- **PORT** - Port number to host the API. _Defaults to `8080`_
> Environment variables can be loaded from a `.env` file.

## Usage
### Health Status
```shell
curl --location 'http://localhost:8080/health'
```

### Upload File
**Fail on existing file**
```shell
curl --location 'http://localhost:8080/upload-file' \
--form 'file=@"~/sample.txt"'
```
**Overwrite existing file**
```shell
curl --location 'http://localhost:8080/upload-file?deleteExisting=true' \
--form 'file=@"~/sample.txt"'
```

## License & copyright
&copy; Vignesh Rao

Licensed under the [MIT License](https://github.com/thevickypedia/api-file-handler/blob/main/LICENSE)
