package ru.study.springboot.util;

import lombok.experimental.UtilityClass;
import ru.study.springboot.error.IllegalRequestDataException;
import ru.study.springboot.error.NotFoundException;
import ru.study.springboot.model.AbstractBaseEntity;

import java.util.Optional;

@UtilityClass
@SuppressWarnings("OptionalUsedAsFieldOrParameterType")
// https://github.com/JetBrains/intellij-community/blob/master/plugins/InspectionGadgets/src/inspectionDescriptions/OptionalUsedAsFieldOrParameterType.html
public class ValidationUtil {

    public static void checkNew(AbstractBaseEntity entity) {
        if (!entity.isNew()) {
            throw new IllegalRequestDataException(entity.getClass().getSimpleName() + " must be new (id=null)");
        }
    }

    public static void checkNotNull(Object obj, String name) {
        if (obj == null) {
            throw new IllegalRequestDataException(name + " must has not null");
        }
    }

    public static void checkSingleModification(int count, String msg) {// delete 1 если ok
        if (count != 1) {
            throw new NotFoundException(msg);
        }
    }

    //  Conservative when you reply, but accept liberally (http://stackoverflow.com/a/32728226/548473)
    public static void assureIdConsistent(AbstractBaseEntity entity, int id) {
        if (entity.isNew()) {
            entity.setId(id);
        } else if (entity.id() != id) {
            throw new IllegalRequestDataException(entity.getClass().getSimpleName() + " must has id=" + id);
        }
    }

    public static <T> T checkNotFoundWithId(Optional<T> optional, Integer id) {
        return checkNotFound(optional, "Entity with id=" + id + " not found");
    }

    public static <T> T checkNotFoundWithName(Optional<T> optional, String name) {
        return checkNotFound(optional, "Entity with name=" + name + " not found");
    }

    public static <T> T checkNotFound(Optional<T> optional, String msg) {
        return optional.orElseThrow(() -> new NotFoundException(msg));
    }

    public static <T> void checkNotDuplicate(Optional<T> optional, String name) {
        if (optional.isPresent())
            throw new IllegalRequestDataException("Entity " + name + " duplicate");
    }
}