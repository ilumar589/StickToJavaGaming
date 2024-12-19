package org.example.book.engine.scene;

import org.eclipse.collections.impl.map.mutable.UnifiedMap;
import org.example.book.engine.graph.Mesh;
import org.jspecify.annotations.NullMarked;

@NullMarked
public record Scene(UnifiedMap<String, Mesh> meshMap) implements AutoCloseable {

    public static Scene getDefaultScene() {
        return new Scene(UnifiedMap.newMap());
    }

    @Override
    public void close() {
        meshMap.values()
                .forEach(Mesh::close);
        meshMap.clear();
    }

   public void addMesh(String meshName, Mesh mesh) {
        meshMap.put(meshName, mesh);
   }
}
