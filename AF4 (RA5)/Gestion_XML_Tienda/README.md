# Gestión de Inventario XML - Tienda

Este proyecto es una aplicación Java por consola para gestionar el inventario de una tienda utilizando un archivo XML local. La aplicación hace uso de la **API DOM (Document Object Model)** de Java para leer, estructurar, modificar y guardar los datos en formato XML.

## Estructura del Proyecto

El proyecto está dividido en varios paquetes para separar las responsabilidades (MVC / DAO):

*   **`modelo`**: Contiene la clase `Producto.java`, que representa la estructura de los datos (ID, nombre, descripción, precio y stock).
*   **`dao`**:
    *   `ProductoXMLDAO.java`: Contiene toda la lógica de operaciones CRUD (Crear, Leer, Actualizar, Borrar) atacando directamente al archivo XML. Gestiona la autogeneración de IDs buscando el valor más alto existente.
    *   `DataLoader.java`: Clase de utilidad que se encarga de inyectar 5 productos de ejemplo (teclados, monitores, etc.) si detecta que el XML está vacío en la primera ejecución.
*   **`conexion`**: Contiene `ConexionXML.java`, encargada de interactuar con el sistema de archivos. Tiene métodos para cargar el árbol DOM a memoria (creando un XML vacío si no existe) y para volcar el árbol modificado de memoria al archivo físico, configurando el *Transformer* para que indente el código (pretty-print).
*   **`colecciones`**: Contiene `GestorColecciones.java`, una clase de demostración que usa `java.io.File` para crear recursivamente directorios (`colecciones_xml/2024`), crear un archivo de prueba dentro y posteriormente eliminarlo todo, verificando que los archivos existen antes de borrarlos.
*   **Directorio raíz**: La clase `Main.java` orquesta la aplicación mediante un bucle con un menú interactivo (`Scanner`) que previene errores si el usuario introduce texto en lugar de números.

## Análisis y Justificación del Formato XML

Como parte del desarrollo, hemos analizado por qué y cuándo usar XML frente a otras alternativas:

### Uso de XML en entornos reales
Aunque en el desarrollo de APIs el JSON es líder, el XML sigue siendo un estándar crucial en muchos entornos empresariales por su robustez:
* **Facturación Electrónica:** Formatos como *Facturae* en España (usado por la Agencia Tributaria) exigen XML. ¿Por qué? Porque el estándar soporta de forma nativa la validación estricta mediante esquemas (XSD) y permite incrustar Firmas Digitales (XMLDSig) directamente en el documento. 
  
  **Ejemplo de un fragmento real de FacturaE que he analizado para esta práctica:**
  ```xml
  <InvoiceTotals>
      <TotalGrossAmountBeforeTaxes>8550.00</TotalGrossAmountBeforeTaxes>
      <TotalTaxOutputs>1966.50</TotalTaxOutputs>
      <InvoiceTotal>10516.50</InvoiceTotal>
  </InvoiceTotals>
  <Items>
      <InvoiceLine>
          <ItemDescription>Vigas de acero</ItemDescription>
          <Quantity>5.0</Quantity>
          <UnitPriceWithoutTax>1700.00</UnitPriceWithoutTax>
          <TotalCost>8500.00</TotalCost>
          <TaxesOutputs>
              <Tax>
                  <TaxTypeCode>01</TaxTypeCode>
                  <TaxRate>23.00</TaxRate>
              </Tax>
          </TaxesOutputs>
      </InvoiceLine>
  </Items>
  ```
*   **Configuraciones complejas:** Archivos como los `pom.xml` de Maven, o configuraciones de servidores (Tomcat, spring properties antiguas) siguen usando XML porque es muy expresivo y permite definir jerarquías complejas y atributos de forma más clara que un simple `.properties`.
*   **Intercambio de datos B2B:** Protocolos como SOAP se basan íntegramente en XML. Muchos bancos y aseguradoras siguen comunicándose mediante SOAP.

### Ventajas e inconvenientes vs otros formatos
*   **Frente a JSON**: El XML es más pesado ("verboso") porque repite las etiquetas de apertura y cierre, lo que consume más ancho de banda. JSON es más ligero y fácil de parsear en el navegador. Sin embargo, XML es mucho más potente para validar datos completos (XSD) y soporta "namespaces" para evitar colisiones de nombres si mezclamos datos de dos empresas distintas en un mismo archivo.
*   **Frente a CSV**: CSV es imbatible en peso y velocidad si solo tenemos datos planos (como una tabla Excel). Pero si los datos tienen jerarquía (ejemplo: una factura que tiene dentro múltiples líneas de detalle, y cada línea tiene múltiples impuestos), en CSV habría que estructurarlo de forma repetitiva, mientras que el XML anida la información de forma natural.
*   **Frente a SQL (Bases de datos relacionales)**: Una BBDD SQL es lo mejor para consultas complejas, relaciones entre miles de tablas y transacciones concurrentes (ACID). Un XML físico como el de esta práctica funciona bien para exportar datos, configuraciones, o almacenar información temporal y ligera, pero no sirve como motor principal de datos si hay miles de usuarios modificando cosas a la vez (habría problemas de concurrencia al sobrescribir el archivo).

### Organización y mantenimiento de Colecciones XML
En el paquete `colecciones` hemos simulado cómo crear una jerarquía de carpetas lógica.
En un entorno de trabajo real, mantener todos los XML en una misma carpeta sería un caos. Lo normal es organizar las colecciones (ya sea en disco o en una BBDD nativa XML) mediante criterios jerárquicos:
1.  **Por año y mes** (ej. `facturas/2024/03/factura_1234.xml`), ideal para archivar datos históricos.
2.  **Por cliente/departamento** (ej. `clientes/CIF999/pedidos/pedido_1.xml`).
Esta forma de organizar las colecciones permite implementar rutinas automáticas de limpieza (borrar o comprimir los directorios de años anteriores para ahorrar espacio) y facilita hacer copias de seguridad incrementales, que es justo la lógica base que demostramos con `GestorColecciones.demostrarColecciones()`.

## Características Técnicas

*   Java 21
*   DocumentBuilderFactory y TransformerFactory (javax.xml.* y org.w3c.dom.*)
*   Gestión de rutas relativas dinámicas (`System.getProperty("user.dir")`) para asegurar que los archivos generados (`datos/productos.xml` y `colecciones_xml`) siempre se guarden dentro del subdirectorio del proyecto, independientemente de cómo se abra en el IDE.
*   Manejo de excepciones al parsear números y rutas de archivos.
