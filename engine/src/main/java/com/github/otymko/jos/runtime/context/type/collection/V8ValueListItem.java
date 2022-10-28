package com.github.otymko.jos.runtime.context.type.collection;

import com.github.otymko.jos.core.IValue;
import com.github.otymko.jos.core.annotation.ContextClass;
import com.github.otymko.jos.core.annotation.ContextProperty;
import com.github.otymko.jos.runtime.context.ContextValue;
import com.github.otymko.jos.runtime.machine.info.ContextInfo;
import lombok.Getter;
import lombok.Setter;

/**
 * Элемент списка значений.
 * Используется для доступа к свойствам и методам элемента списка значений.
 *
 * @see V8ValueList
 */
@ContextClass(name = "ЭлементСпискаЗначений", alias = "ValueListItem")
public class V8ValueListItem extends ContextValue {
    public static final ContextInfo INFO = ContextInfo.createByClass(V8ValueListItem.class);

    @Getter
    private final V8ValueList owner;

    @Getter
    @Setter
    @ContextProperty(name = "Значение", alias = "Value")
    private IValue value;

    @Getter
    @Setter
    @ContextProperty(name = "Представление", alias = "Presentation")
    private String presentation;

    @Getter
    @Setter
    @ContextProperty(name = "Пометка", alias = "Check")
    private Boolean check;

    @Getter
    @Setter
    @ContextProperty(name = "Картинка", alias = "Picture")
    private IValue picture;

    public static V8ValueListItem constructor(V8ValueList owner, IValue value, String presentation, Boolean check, IValue picture) {
        return new V8ValueListItem(owner, value, presentation, check, picture);
    }

    public V8ValueListItem(V8ValueList owner, IValue value, String presentation, Boolean check, IValue picture) {

        if (check == null)
            check = Boolean.FALSE;

        if (presentation == null)
            presentation = "";

        this.owner = owner;
        this.value = value;
        this.presentation = presentation;
        this.check = check;
        this.picture = picture;
    }

    public V8ValueListItem(V8ValueList owner, IValue value) {
        this.owner = owner;
        this.value = value;
        this.presentation = "";
        this.check = Boolean.FALSE;
    }

    @Override
    public ContextInfo getContextInfo() {
        return INFO;
    }


}
