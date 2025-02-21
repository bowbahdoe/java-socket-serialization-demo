package server;

public record Acknowledged(
        String message
) implements ServerToClientMessage {
}
