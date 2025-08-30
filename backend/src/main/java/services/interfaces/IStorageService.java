package services.interfaces;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface IStorageService {
    /**
     * Sube un archivo a un servicio de almacenamiento.
     * @param file El archivo a subir.
     * @param key La clave o nombre del archivo en el servicio de almacenamiento.
     * @return Una URL o un mensaje de éxito.
     */
    String uploadFile(MultipartFile file, String key) throws IOException;

    /**
     * Descarga un archivo de un servicio de almacenamiento.
     * @param key La clave o nombre del archivo a descargar.
     * @return El archivo en un array de bytes.
     */
    byte[] downloadFile(String key) throws IOException;
}
