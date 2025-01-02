package org.example.book.engine.graph;

import org.eclipse.collections.api.factory.primitive.IntLists;
import org.eclipse.collections.api.list.primitive.ImmutableFloatList;
import org.eclipse.collections.api.list.primitive.ImmutableIntList;
import org.jspecify.annotations.NullMarked;
import org.lwjgl.opengl.GL30;

import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glVertexAttribPointer;
import static org.lwjgl.opengl.GL30.*;

@NullMarked
public record Mesh(
        int numberOfVertices,
        int vertexArrayObjectId,
        ImmutableIntList vertexBufferObjectIds
) implements AutoCloseable {

    public static Mesh create(ImmutableFloatList positions,
                              ImmutableFloatList colors,
                              ImmutableIntList indices) {
        int vaoId = glGenVertexArrays();
        glBindVertexArray(vaoId);

        final var vboIdList = IntLists.mutable.empty();

        // Positions vbo
        int vboId = glGenBuffers();
        vboIdList.add(vboId);
        glBindBuffer(GL_ARRAY_BUFFER, vboId);
        glBufferData(GL_ARRAY_BUFFER, positions.toArray(), GL_STATIC_DRAW);
        glEnableVertexAttribArray(0);
        glVertexAttribPointer(0, 3, GL_FLOAT, false, 0, 0);

        // color vbo
        vboId = glGenBuffers();
        vboIdList.add(vboId);
        glBindBuffer(GL_ARRAY_BUFFER, vboId);
        glBufferData(GL_ARRAY_BUFFER, colors.toArray(), GL_STATIC_DRAW);
        glEnableVertexAttribArray(1);
        glVertexAttribPointer(1, 3, GL_FLOAT, false, 0, 0);

        // index vbo
        vboId = glGenBuffers();
        vboIdList.add(vboId);
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, vboId);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, indices.toArray(), GL_STATIC_DRAW);


        glBindBuffer(GL_ARRAY_BUFFER, 0);
        glBindVertexArray(0);

        return new Mesh(indices.size(), vaoId, vboIdList.toImmutable());
    }

    @Override
    public void close() {
        vertexBufferObjectIds.forEach(GL30::glDeleteBuffers);
        glDeleteVertexArrays(vertexArrayObjectId);
    }
}
