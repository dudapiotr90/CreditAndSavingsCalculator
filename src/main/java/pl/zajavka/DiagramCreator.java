package pl.zajavka;

import de.elnarion.util.plantuml.generator.classdiagram.PlantUMLClassDiagramGenerator;
import de.elnarion.util.plantuml.generator.classdiagram.config.PlantUMLClassDiagramConfigBuilder;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class DiagramCreator {

    private static final Path DIAGRAM_PATH = Paths.get("./results");

    public static void main(String[] args) throws IOException {

        String diagramFileName = "bankCalculator.puml";
        String packageToGenerateDiagram = "pl.zajavka.bankCalculator";

        Optional<String> generatedDiagram = generateDiagramAsText(packageToGenerateDiagram);

        if (generatedDiagram.isPresent()) {
            writeDiagramToFile(generatedDiagram.get(), diagramFileName);
        }
    }

    private static Optional<String> generateDiagramAsText(String packageToGenerateDiagram) {
        List<String> scanPackages = new ArrayList<>();
        scanPackages.add(packageToGenerateDiagram);

        var config = new PlantUMLClassDiagramConfigBuilder(scanPackages)
            .withRemoveFields(true)
            .withHideMethods(true)
            .build();
        var generator = new PlantUMLClassDiagramGenerator(config);

        try {
            return Optional.of(generator.generateDiagramText()
                .replace(packageToGenerateDiagram+".",""));
        } catch (ClassNotFoundException | IOException e) {
            System.err.printf("Error generating diagram, %s", e);
        }
        return Optional.empty();
    }

    private static void writeDiagramToFile(String generatedDiagram, String diagramFileName) throws IOException {
        if (!Files.exists(DIAGRAM_PATH)) {
            Files.createDirectories(DIAGRAM_PATH);
        }
        Path umlDiagramFilePath = DIAGRAM_PATH.resolve(diagramFileName);
        if (Files.deleteIfExists(umlDiagramFilePath)) {
            Files.createFile(umlDiagramFilePath);
        }
        Files.writeString(umlDiagramFilePath, generatedDiagram);

    }
}
