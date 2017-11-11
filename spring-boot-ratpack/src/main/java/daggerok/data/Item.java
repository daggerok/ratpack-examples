package daggerok.data;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;

@Data
@Document
@NoArgsConstructor
@Accessors(chain = true)
@RequiredArgsConstructor(staticName = "of")
public class Item implements Serializable {
    private static final long serialVersionUID = 4865159616229095622L;
    public static String MODEL_LIST_NAME = "items";

    @Id String id;
    @NonNull String content;
}
