package client;

public record Hello(String name)
        implements ClientToServerMessage {
}
