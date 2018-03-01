# JaKoImage2PDF
[![License: AGPL v3](https://img.shields.io/badge/License-AGPL%20v3-blue.svg)](https://www.gnu.org/licenses/agpl-3.0) [![build status](https://travis-ci.org/nihas101/java-kotlin-image-to-pdf-converter.svg?branch=master)](https://travis-ci.org/nihas101/java-kotlin-image-to-pdf-converter) [![Maintainability](https://api.codeclimate.com/v1/badges/32c17125b13bb7c177bc/maintainability)](https://codeclimate.com/github/nihas101/java-kotlin-images-to-pdf-converter/maintainability) 
[![codecov](https://codecov.io/gh/nihas101/java-kotlin-image-to-pdf-converter/branch/master/graph/badge.svg)](https://codecov.io/gh/nihas101/java-kotlin-image-to-pdf-converter)

JaKoImage2PDF is a program for converting images to PDFs with the use of [iText 7](https://itextpdf.com/).

[Click here](https://github.com/nihas101/java-kotlin-image-to-pdf-converter/releases/latest) for the latest release.

## Features

- Build a single PDF from a directory of images [See here](https://github.com/nihas101/java-kotlin-images-to-pdf-converter/blob/master/README.md#how-to-build-a-single-pdf)
- Build multiple PDFs from multiple subdirectories of images at once [See here](https://github.com/nihas101/java-kotlin-images-to-pdf-converter/blob/master/README.md#how-to-build-multiple-pdfs-at-once)
- Build PDFs from one or multiple ZIP files of images
- Change the order of the images in the PDF via drag-and-drop and remove unwanted ones
- Text-only-mode

## Requirements
**Java 1.8** is required to run this program.

## Build Standalone Distribution

To create a standalone distribution as a zip or tar file, run:

```sh
./gradlew distZip
```
or
```sh
./gradlew distTar
```

The distribution is placed under `build/distributions`.

## How to run the converter

To run the program from the distribution, extract the zip or tar file, and run the launch script for your system in the bin folder by double-clicking on it or typing either:
```sh
./jaKoImage2Pdf
```
in the command-line interface or
```sh
./jaKoImage2Pdf --noGUI
```
to run the program in text-only-mode.

## How to build a single PDF

### In GUI-mode

1. Run the program.
2. Simply drag and drop the directory you want to convert **or** click on ![choosedirectorybutton](https://user-images.githubusercontent.com/19901622/35849256-453993be-0b21-11e8-924c-b04b2e69e1b3.PNG) and select the source-directory of images you want to convert into a PDF.
3. Reorder images and remove unwanted ones.
4. Click on ![buildbutton](https://user-images.githubusercontent.com/19901622/35849742-e93f197e-0b22-11e8-8dab-4185c43171eb.PNG). After picking a target-directory the PDF will be build and placed at that location.

### In text-only-mode

1. Run the program in text-only-mode.
2. Supply a path to the folder of images you want to convert into a PDF
3. Type ``no`` when asked "Do you want to build multiple PDFs from this source?"
4. Type ``yes`` if the file is a ZIP-file, ``no`` otherwise
5. Reorder images and remove unwanted ones.
6. Type ``build``
7. Type ``no`` to build the PDF in the same folder as was supplied
8. Type ``exit`` to exit the program **or** go to step 2 to build more PDFs

## How to build multiple PDFs at once

### In GUI-mode

1. Run the program.
2. Click on ![gear](https://user-images.githubusercontent.com/19901622/35849782-0f48b198-0b23-11e8-8599-15fbdd355392.PNG) and check the "Build PDFs from multiple directories"-box.
3. Simply drag and drop the directory of subdirectories you want to convert **or** click on ![choosedirectorybutton](https://user-images.githubusercontent.com/19901622/35849256-453993be-0b21-11e8-924c-b04b2e69e1b3.PNG) and select the source-directory of subdirectories you want to convert into PDFs.
4. If you want to alter specific PDFs to be build, double-click on the directory you want to alter to open a second window with it's contents.
    1. Reorder images and remove unwanted ones
    2. Click on the build button in the second window to build the PDF and remove this directory from the main window
5. Click on ![buildbutton](https://user-images.githubusercontent.com/19901622/35849742-e93f197e-0b22-11e8-8dab-4185c43171eb.PNG) to build multiple PDFs from the chosen directories. After picking a target-directory the PDFs will be build and placed at that location

### In text-only-mode

1. Run the program in text-only-mode.
2. Supply a path to the directory of subdirectories you want to convert into PDFs
3. Type ``yes`` when asked "Do you want to build multiple PDFs from this source?"
4. Type ``yes`` if the files are ZIP-files, ``no`` otherwise
5. Reorder images and remove unwanted ones.
6. Type ``build``
7. Type ``no`` to build the PDF in the same folder as was supplied
8. Type ``exit`` to exit the program **or** go to step 2 to build more PDFs
