# Convert XML to Docx

This project is a Java application that converts XML data into a Docx document. It uses Apache POI to handle Docx files and JAXB for XML processing. The application reads an XML file, parses the data, and then populates a Docx template with the parsed data.

## Features

- **XML Parsing**: The application uses JAXB to parse XML data into Java objects.
- **Docx Manipulation**: The application uses Apache POI to read and write Docx files.
- **Placeholder Replacement**: The application can replace placeholders in the Docx template with data from the XML file.
- **Image Insertion**: The application can replace specific placeholders with an image, such as a company logo.

## Usage

To use the application, follow these steps:

1. Create an XML file with the data you want to include in the Docx document.
2. Create a Docx template with placeholders for the data in the XML file.
3. Run the application and provide the XML file, Docx template, and output file as arguments.
4. The application will read the XML file, parse the data, and populate the Docx template with the data.
5. The output file will contain the final Docx document with the data from the XML file.

## Example

Suppose you have an XML file with the following data:

```xml
<document>
    <title>Example Document</title>
    <content>This is an example document created using XML and Docx.</content>
</document>
```

And a Docx template with the following placeholders:

- `<title>`: Placeholder for the document title.
- `<content>`: Placeholder for the document content.