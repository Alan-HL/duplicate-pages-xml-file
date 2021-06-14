package com.identifix.duplicatepagesxmlfile.proposal

import groovy.util.logging.Slf4j
import org.springframework.stereotype.Component

@Component
@Slf4j
class DuplicatePages {

    void readXmlFiles(String files){
        try {
            List listFiles = getFiles(files)
            List generalData = []
            listFiles.eachWithIndex{ filePath, i ->
                getDataFile(filePath.toString(), "file${i+1}.txt", generalData)
            }
            generalData = getDuplicateData(generalData)
            saveData(generalData, "compare.txt")
        }
        catch (Exception e) {
            log.error("error: $e")
        }
    }

    List getFiles(String files){
        List filePaths = []
        files.split("\r\n").each { filePath ->
            filePaths.push(filePath)
        }
    }

    List getDataFile(String filePath, String newFileName, List generalData){
        try {
            log.info("opening file: $filePath")
            List duplicated, data = []
            File file = new File(filePath)
            FileReader fr = new FileReader (file)
            BufferedReader br = new BufferedReader(fr)
            String line, pageName
            while((line=br.readLine())!=null){
                line.split("<ptxt>").each { subLine ->
                    if(subLine.contains("</ptxt>")){
                        pageName = subLine.split("</ptxt>")[0]
                        data.push(pageName)
                    }
                }
            }
            duplicated = getDuplicateData(data)
            generalData = updateGeneralData(generalData, data)
            println("data size: " + data.size())
            println("duplicated size: " + duplicated.size())
            println("general size: " + generalData.size())
            saveData(duplicated, newFileName)
            return  generalData

        }
        catch (Exception e) {
            log.error("error: " + e)
        }

    }

    static List getDuplicateData( List data){
        List duplicated = []
        data.each { value ->
            if(Collections.frequency(data,value) > 1 && !duplicated.contains(value)){
                duplicated.push(value)
            }
        }
        return duplicated
    }

    static List updateGeneralData( List generalData, List data){
        data.stream().distinct().each { value ->
            generalData.push(value)
        }
        return generalData
    }

    private static void saveData(List data, String fileName){
        String outputPath = "src/main/resources/"
        File file = new File(outputPath + fileName)
        validateFile(file)
        FileWriter writeFile = new FileWriter(file, true)
        data.each { value ->
            writeFile.write("$value \n")
        }
        writeFile.close()
    }

    private static void validateFile(File file) {
        if (file.exists()) {
            file.delete()
            file.createNewFile()
        }
    }

}
