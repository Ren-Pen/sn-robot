package top.bioelectronic.sdk.robot.messages;

import top.bioelectronic.sdk.robot.messages.content.*;
import lombok.NoArgsConstructor;
import top.bioelectronic.sdk.robot.messages.content.*;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Objects;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@NoArgsConstructor
public class SNMessageChain extends ArrayList<SNMessage> implements Plus {


    public SNMessageChain(SNMessage... message){
        addAll(Arrays.asList(message));
    }


    @Override
    public boolean add(SNMessage SNMessage) {
        if (SNMessage == null) return false;
        return super.add(SNMessage);
    }

    @Override
    public void add(int index, SNMessage element) {
        if (element == null) return;
        super.add(index, element);
    }

    @Override
    public boolean addAll(Collection<? extends SNMessage> c) {
        return super.addAll(c.stream().filter((Predicate<SNMessage>) Objects::nonNull).collect(Collectors.toList()));
    }

    @Override
    public boolean addAll(int index, Collection<? extends SNMessage> c) {
        return super.addAll(index, c.stream().filter((Predicate<SNMessage>) Objects::nonNull).collect(Collectors.toList()));
    }

    public SNMessage[] get(Class<? extends SNMessage> msgClass) {
        return this.stream().filter(miraiMessage -> msgClass.isAssignableFrom(miraiMessage.getClass())).toArray(SNMessage[]::new);
    }

    public SNMessageChain plus(SNMessage SNMessage){
        add(SNMessage);
        return this;
    }

    @Override
    public SNMessageChain text(String text) {
        return plus(new SNText(text));
    }

    @Override
    public SNMessageChain text(String text, Object... args) {
        return plus(new SNText(text, args));
    }

    @Override
    public SNMessageChain atAll() {
        return plus(new SNAtAll());
    }

    @Override
    public SNMessageChain at(long target) {
        return plus(new SNAt(target));
    }

    @Override
    public SNMessageChain face(int id) {
        return plus(new SNFace(id));
    }

    @Override
    public SNMessageChain image(String id) {
        return plus(new SNImage(id));
    }

    public boolean contains(Class<? extends SNMessage> clazz){
        return this.stream().anyMatch(miraiMessage -> miraiMessage.getClass() == clazz);
    }

    public boolean contains(SNMessage message){
        return this.stream().anyMatch(miraiMessage -> miraiMessage.equals(message));
    }

}
