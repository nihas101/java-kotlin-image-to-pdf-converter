# Images To PDF Converter [![build status](https://travis-ci.org/nihas101/java-kotlin-images-to-pdf-converter.svg?branch=master)](https://travis-ci.org/nihas101/java-kotlin-images-to-pdf-converter)

This is a program for converting images to PDFs with the use of [iText 7](https://itextpdf.com/).

## Features

- Build a single PDF from a directory of images [See here](https://github.com/nihas101/java-kotlin-images-to-pdf-converter/blob/master/README.md#how-to-build-a-single-pdf)
- Build multiple PDFs from multiple subdirectories of images at once [See here](https://github.com/nihas101/java-kotlin-images-to-pdf-converter/blob/master/README.md#how-to-build-multiple-pdfs-at-once)
- Change the order the images will appear in in the final PDF via drag-and-drop or remove them

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
./images2PdfConverter
```
in the command-line interface or
```sh
./images2PdfConverter --noGUI
```
to run the program in text-mode.

## How to build a single PDF

### In GUI-mode

1. Run the program
2. Click on ![choosedirectorybutton](https://user-images.githubusercontent.com/19901622/35849256-453993be-0b21-11e8-924c-b04b2e69e1b3.PNG) and choose the source-directory of images you want to convert into a PDF.
3. Reorder images and remove unwanted ones
4. Click on ![buildbutton](https://user-images.githubusercontent.com/19901622/35849742-e93f197e-0b22-11e8-8dab-4185c43171eb.PNG). After picking a target-directory the PDF will be build and placed at that location

### In text-mode

_TODO_

## How to build multiple PDFs at once

### In GUI-mode

1. Run the program
2. Click on ![gear](https://user-images.githubusercontent.com/19901622/35849782-0f48b198-0b23-11e8-8599-15fbdd355392.PNG) and check the "Build PDFs from multiple directories"-box
3. Click on ![choosedirectorybutton](https://user-images.githubusercontent.com/19901622/35849256-453993be-0b21-11e8-924c-b04b2e69e1b3.PNG) and choose the source-directory which subdirectories you want to convert into a PDF.
4. If you want to alter specific PDFs to be build, double-click on the directory you want to alter to open a second window with it's contents.
    1. Reorder images and remove unwanted ones
    2. Click on the build button in the second window to build the PDF and remove this directory from the main window
5. Click on ![buildbutton](https://user-images.githubusercontent.com/19901622/35849742-e93f197e-0b22-11e8-8dab-4185c43171eb.PNG) to build multiple PDFs from the chosen directories. After picking a target-directory the PDF will be build and placed at that location

### In text-mode

_TODO_
