package de.biosphere.spoticord.commands;

import de.biosphere.spoticord.Configuration;
import de.biosphere.spoticord.Spoticord;
import net.dv8tion.jda.api.entities.Message.MentionType;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CommandManager extends ListenerAdapter {

    private static final Logger logger = LoggerFactory.getLogger(CommandManager.class);
    private static final Pattern MENTION_PATTERN = MentionType.USER.getPattern();

    private final Set<Command> availableCommands;

    public CommandManager(final Spoticord bot) {
        this.availableCommands = new HashSet<>();
        final Set<Class<? extends Command>> classes = new Reflections("de.biosphere.spoticord.commands")
                .getSubTypesOf(Command.class);
        for (Class<? extends Command> cmdClass : classes) {
            try {
                final Command command = cmdClass.getDeclaredConstructor().newInstance();
                command.setInstance(bot);
                if (availableCommands.add(command)) {
                    logger.info("Registered " + command.getCommand() + " Command");
                }
            } catch (Exception exception) {
                logger.error("Error while registering Command!", exception);
            }
        }
        bot.getJDA().addEventListener(this);
    }

    @Override
    public void onGuildMessageReceived(final GuildMessageReceivedEvent event) {
        if (event.getAuthor().isBot()) {
            return;
        }
        final String content = event.getMessage().getContentRaw();
        final PrefixType prefixType = checkPrefix(content, event.getGuild().getSelfMember().getId());
        if (prefixType != PrefixType.NONE) {
            final Optional<Command> optional = availableCommands.stream()
                    .filter(command -> command.getCommand().equalsIgnoreCase(getCommand(content, prefixType)))
                    .findFirst();
            optional.ifPresent(command -> command.execute(getArguments(content, prefixType), event.getMessage()));
        }
    }

    private PrefixType checkPrefix(final String content, final String botId) {
        if (content.split(" ").length > 1) {
            final Matcher matcher = MENTION_PATTERN.matcher(content.split(" ")[0]);
            if (matcher.matches() && matcher.group(1).equals(botId)) {
                return PrefixType.MENTION;
            }
        }
        if (Configuration.DISCORD_PREFIX != null && content.startsWith(Configuration.DISCORD_PREFIX)) {
            return PrefixType.PREFIX;
        }
        return PrefixType.NONE;
    }

    private String[] getArguments(final String content, final PrefixType prefixType) {
        final String[] arguments = content.split(" ");
        return Arrays.copyOfRange(arguments, prefixType == PrefixType.PREFIX ? 1 : 2, arguments.length);
    }

    private String getCommand(final String content, final PrefixType prefixType) {
        final String[] arguments = content.split(" ");
        if (prefixType == PrefixType.PREFIX) {
            return arguments[0].replaceFirst("\\" + Configuration.DISCORD_PREFIX, "");
        }
        if (arguments.length >= 2) {
            return arguments[1];
        }
        return null;
    }

    public Set<Command> getAvailableCommands() {
        return Collections.unmodifiableSet(availableCommands);
    }

    private enum PrefixType {
        MENTION, PREFIX, NONE
    }

}