/*
 * Copyright Samuel Halliday 2011
 * PROPRIETARY/CONFIDENTIAL. Use is subject to licence terms.
 */
package uk.me.fommil.zibaldone.importer;

import com.google.common.base.Charsets;
import uk.me.fommil.zibaldone.Note;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.logging.Logger;
import java.util.regex.Pattern;
import com.google.common.base.Preconditions;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.io.Closeables;
import com.google.common.io.Files;
import java.util.*;
import uk.me.fommil.zibaldone.*;

/**
 * Parse Emacs <a href="http://orgmode.org/">Org-Mode</a> files.
 *
 * @author Samuel Halliday
 */
public class OrgModeImporter implements Importer {

    private static final Pattern startPattern = Pattern.compile("^\\*+\\s");

    private static final Logger log = Logger.getLogger(OrgModeImporter.class.getName());

    private volatile File file;

    /**
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {

        OrgModeImporter importer = new OrgModeImporter();
        Properties properties = new Properties();
        properties.setProperty("filename", "../data/QT2-notes.org");
        importer.setProperties(properties);
        List<Note> notes = importer.getNotes();

        Reconciler reconciler = new Reconciler();
        reconciler.reconcile(importer, notes);
    }

    @Override
    public String getInstanceName() {
        return getName() + ":" + file.getPath().hashCode();
    }

    @Override
    public String getName() {
        return "OrgMode";
    }

    @Override
    public List<Note> getNotes() throws IOException {
        BufferedReader reader = Files.newReader(file, Charsets.UTF_8);
        try {
            List<Note> notes = Lists.newArrayList();
            StringBuilder rawNote = new StringBuilder();
            Map<Integer, Note> hierarchy = Maps.newTreeMap();
            String line;
            while ((line = reader.readLine()) != null) {
                if (startPattern.matcher(line).find()) {
                    Entry<Integer, Note> note = parseNote(rawNote.toString(), hierarchy);
                    if (!note.getValue().getTitle().isEmpty()) {
                        notes.add(note.getValue());
                        log.fine(note.toString());
                        hierarchy.put(note.getKey(), note.getValue());
                    }
                    rawNote = new StringBuilder();
                }
                rawNote.append(line);
                rawNote.append("\n");
            }
            return notes;
        } finally {
            Closeables.closeQuietly(reader);
        }
    }

    // integer key represents the depth of the note
    private Entry<Integer, Note> parseNote(String rawNote, Map<Integer, Note> hierarchy) throws IOException {
        BufferedReader reader = new BufferedReader(new StringReader(rawNote));
        String header = reader.readLine();

        Integer depth = header.indexOf(" ");

        List<String> headerParts = Arrays.asList(header.split(":"));
        String title = headerParts.get(0).replace("*", "").trim();
        Iterable<String> strings = Iterables.skip(headerParts, 1);
        Set<Tag> tags = Tag.asTags(strings);
        if (hierarchy.containsKey(depth - 1)) {
            tags.addAll(hierarchy.get(depth - 1).getTags());
        }

        String contents = new String(rawNote.substring(header.length() + 1));

        Note note = new Note();
        note.setTitle(title);
        note.setTags(tags);
        note.setContents(contents);

        return Maps.immutableEntry(depth, note);
    }

    @Override
    public Collection<String> getPropertyNames() {
        return Collections.singleton("filename");
    }

    @Override
    public void setProperties(Properties properties) {
        Preconditions.checkNotNull(properties);
        String filename = properties.getProperty("filename");
        Preconditions.checkNotNull(filename);

        file = new File(filename);
        Preconditions.checkArgument(file.isFile(), file);
    }
}
