# System Design Interview Prep — Context

## Purpose

This repository is a personal **Staff / Senior Staff Software Engineer interview-preparation library**.

The goal is to build practical, interview-ready system-design material rather than generic textbook notes.

The material should emphasize:

- Requirements clarification
- Scale estimation
- Architecture
- API and data-model decisions
- Distributed-systems behavior
- Concurrency and consistency
- Failure modes
- Performance and bottlenecks
- Observability
- Security/reliability considerations
- Cost and operational trade-offs
- Multi-region design
- Evolution from a simple design to production scale
- Java examples that are easy to explain during an interview

The preferred programming language is **Java**.

---

## User Engineering Background

The user is a **Senior Staff Engineer at PayPal** and is preparing for Staff/Senior Staff-level software engineering interviews.

Earlier experience includes Principal Software Engineer / Principal Backend Java Engineer work at Priceline.

Approximate experience: **11+ years**.

Strong technical areas:

- Java
- Spring Boot
- Distributed systems
- Kafka
- Kafka Streams
- Kafka Connect
- GCP
- GKE
- Firestore
- BigQuery
- GCS
- Pub/Sub
- Solr / Lucene
- OpenSearch
- Spark
- Apache Beam
- Flink
- gRPC
- REST
- Vertex AI / GenAI

Relevant real-world projects include:

### Kafka Platform

Led a Kafka streaming platform team.

Work included:

- Migration from legacy Kafka infrastructure to Kafka SaaS / Aiven
- Kafka upgrades
- Broker health dashboards and alerts
- Confluent/Aiven support
- Self-service Jira tooling for topic/schema/ACL creation
- Reusable downstream consumers
- Integrations to Splunk, GCS, BigQuery, MySQL, and Oracle
- Producer/consumer wrapper libraries

### Search / Typeahead

Worked on Priceline autocomplete/typeahead using Solr/Lucene.

Key themes:

- Single-digit millisecond p99 latency
- Personalized location recommendations
- Ranking validation
- Parity testing
- Calibration
- Production metrics and Splunk dashboards

### GenAI Travel Guide

Designed a GenAI-generated travel-guide system using GCP Vertex AI.

Relevant architecture:

- Hierarchical location model
- Firestore / NoSQL
- Example hierarchy: USA → Nevada → Las Vegas → Vegas Strip
- Node.js pages
- Fastly CDN
- Scale around 100k cities/locations

### Coupons Platform

Worked on a large coupons portal.

Relevant scale/design topics:

- Firestore
- Approximately 15M users
- Multiple campaigns
- Thousands of coupons per scheme
- Need to scale toward millions of coupons per campaign
- Multiple campaigns concurrently
- Technical roadmap and component design

### Marketing Technology

Relevant systems include:

- Tealium
- GrowthLoop
- Iterable
- Customer journeys
- Promotional/booking emails
- Audience creation
- Campaign throttling
- Personalization
- Fastly attribution data
- GCS pipelines

---

# Interview Preparation Framework

A strong Staff-level system-design answer should generally follow this structure:

## 1. Clarify Requirements

Ask:

- Who are the users/clients?
- What are the primary use cases?
- What is in scope?
- What is explicitly out of scope?
- What are latency requirements?
- What are availability requirements?
- What consistency guarantees are required?
- What retention is required?
- What are security/privacy constraints?

Avoid spending too much time asking questions. State reasonable assumptions when appropriate.

## 2. Estimate Scale

Estimate:

- DAU / users
- Requests per second
- Peak QPS
- Read/write ratio
- Data size
- Growth
- Network bandwidth
- Cache size
- Storage requirements

Use simple calculations and clearly state assumptions.

## 3. High-Level Architecture

Start simple.

Typical flow:

Client
→ API Gateway / Load Balancer
→ Stateless Services
→ Cache / Database / Message Queue
→ Async workers / downstream systems

Then explain how each component supports a requirement.

## 4. Data Model / APIs

Define:

- Main entities
- Primary keys
- Partitioning keys
- Important indexes
- API contracts
- Idempotency behavior

## 5. Scaling

Discuss:

- Horizontal scaling
- Caching
- Partitioning/sharding
- Replication
- Async processing
- Backpressure
- Connection pooling
- Hot-key mitigation

## 6. Failure Modes

Always discuss:

- Dependency outage
- Database failure
- Cache failure
- Network partition
- Duplicate requests
- Retries
- Timeouts
- Partial failures
- Overload
- Hot partitions
- Data corruption
- Recovery

## 7. Trade-offs

Explicitly compare:

- Consistency vs availability
- Latency vs correctness
- Cost vs performance
- Simplicity vs flexibility
- Centralized vs distributed state
- Strong vs eventual consistency

## 8. Evolution

Explain how the initial design evolves as traffic grows.

A Staff-level answer should demonstrate that the design is not static.

---

# Current Repository

Repository:

https://github.com/pranavnandedkar/system-design

Current important files:

- index.html
- rate-limiter.html
- rate-limiter/java/FixedWindowRateLimiter.java
- rate-limiter/java/SlidingWindowLogRateLimiter.java
- rate-limiter/java/SlidingWindowCounterRateLimiter.java
- rate-limiter/java/TokenBucketRateLimiter.java
- rate-limiter/java/LeakyBucketRateLimiter.java
- system-design-context.md

GitHub Pages is intended to expose the repository as a study website.

Expected root URL after GitHub Pages is configured:

https://pranavnandedkar.github.io/system-design/

Rate limiter page:

https://pranavnandedkar.github.io/system-design/rate-limiter.html

---

# Current Landing Page

index.html is a System Design Study Hub.

It contains:

- Staff Engineer system-design framework
- Link to Rate Limiter deep dive
- Architecture practice section
- Distributed systems practice section
- Communication guidance
- Production-oriented study guidance

The landing page is intended to grow as more system-design topics are added.

---

# Rate Limiter Design

The current deep-dive page is:

rate-limiter.html

It covers:

1. Requirements
2. Clarification questions
3. Reference architecture
4. Fixed Window
5. Sliding Window Log
6. Sliding Window Counter
7. Token Bucket
8. Leaky Bucket
9. Algorithm comparison
10. Java implementations
11. Distributed Redis design
12. Atomic Redis/Lua approach
13. HTTP 429 behavior
14. Retry-After
15. Failure handling
16. Fallback strategies
17. Hot keys
18. Memory considerations
19. Multi-region considerations
20. Observability
21. Staff-level interview questions
22. Model interview closing answer

---

# Rate Limiter Java Examples

## FixedWindowRateLimiter.java

Uses:

ConcurrentHashMap<String, Counter>

Concept:

- Divide time into fixed intervals
- Maintain request count per key/window
- Reject when limit is exceeded

Main issue:

A burst can occur around the boundary between two windows.

---

## SlidingWindowLogRateLimiter.java

Uses:

ConcurrentHashMap<String, Deque<Long>>

Concept:

- Store timestamps of recent requests
- Remove timestamps outside the sliding window
- Allow when the number of remaining timestamps is below the limit

The example synchronizes access to each queue.

Main trade-off:

High memory usage when request volume is high because individual timestamps are stored.

---

## SlidingWindowCounterRateLimiter.java

Uses a simplified two-window counter.

Concept:

- Current window count
- Previous window count
- Weighted estimate of requests in the current sliding window

This is intentionally educational rather than production-grade.

---

## TokenBucketRateLimiter.java

Uses:

- Capacity
- Current token count
- Refill rate
- Last refill timestamp

Concept:

Requests consume tokens.

Tokens are replenished continuously.

Advantages:

- Allows controlled bursts
- Smooth long-term rate
- Common production approach

---

## LeakyBucketRateLimiter.java

Uses a queue-style model.

Concept:

- Requests enter a bounded queue
- Requests leave at a controlled rate
- Queue overflow causes rejection

The current example is educational.

A real implementation would normally have a worker/scheduler consuming the queue.

---

# Distributed Rate Limiter Design

For production distributed environments, the preferred discussion is:

Client
→ Load Balancer
→ Multiple application instances
→ Shared Redis-based rate limiter
→ Downstream service

Example Redis key:

rate:{tenantId}:{endpoint}

Important requirement:

The check-and-update operation must be atomic.

Possible approaches:

- Redis Lua script
- Atomic Redis commands
- Carefully designed transactions where appropriate

Avoid:

GET count
→ application logic
→ SET count

because multiple application instances can race.

---

# HTTP Behavior

When the limit is exceeded:

HTTP status:

429 Too Many Requests

Useful response header:

Retry-After

Possible response body:

{
  "error": "rate_limit_exceeded",
  "message": "Too many requests"
}

---

# Production Topics to Discuss

## Key Dimensions

Rate limiting can be applied by:

- User
- API key
- Tenant
- IP address
- Endpoint
- Region
- Service

Be explicit about the chosen dimension.

## Hot Keys

A large tenant or popular API key can create a Redis hot key.

Possible mitigations:

- Local pre-filtering
- Key sharding
- Hierarchical rate limiting
- Per-instance limits combined with global limits
- Traffic isolation

But every mitigation changes accuracy/complexity.

## Redis Failure

Discuss whether the system should:

- Fail open
- Fail closed
- Use a local fallback
- Use a degraded approximate limiter

Decision depends on endpoint criticality.

For security-sensitive APIs, fail-closed behavior may be more appropriate.

For availability-sensitive APIs, fail-open or degraded local behavior may be preferable.

## Multi-Region

Questions:

- Is the limit global or regional?
- Can traffic be independently limited in each region?
- Is exact global enforcement required?
- Is eventual approximation acceptable?

Possible architectures:

1. Regional Redis
2. Global datastore
3. Token allocation per region
4. Hierarchical global + local limits

Exact global rate limiting across regions increases latency and coordination complexity.

---

# Observability

Important metrics:

- Requests allowed
- Requests rejected
- Rejection rate
- Rate-limit latency
- Redis latency
- Redis errors
- Hot keys
- Per-tenant request rate
- Per-endpoint request rate
- Token/bucket utilization
- Fallback activation
- 429 response rate

Useful dashboards should support:

- Tenant-level analysis
- Endpoint-level analysis
- Region-level analysis
- Time-series spikes
- Dependency health

---

# Staff-Level Rate Limiter Interview Questions

Be prepared to answer:

1. Why Token Bucket instead of Fixed Window?
2. How do you make the limiter distributed?
3. What happens if Redis goes down?
4. How do you prevent race conditions?
5. How do you handle hot keys?
6. How do you support millions of users?
7. How do you implement global rate limiting across regions?
8. How do you avoid a thundering herd?
9. How do you expose retry information to clients?
10. How do you test correctness?
11. How do you monitor the system?
12. How do you roll out a new limit safely?
13. How do you support different limits per customer tier?
14. How would you implement burst capacity?
15. What happens during a regional outage?

---

# Coding Style

Prefer:

- Java
- Simple readable classes
- Standard Java collections
- Clear naming
- Minimal dependencies
- Interview-friendly implementations

Do not over-engineer the sample code.

Clearly distinguish:

Educational implementation

from

Production implementation

For production examples, explain concurrency, distributed atomicity, eviction, persistence, monitoring, and failure handling.

---

# Future System Design Topics

Potential next topics:

- Distributed cache
- URL shortener
- Notification system
- Distributed scheduler
- Job queue
- Kafka-based event platform
- Search/autocomplete
- Distributed lock
- Leader election
- File/object storage
- Metrics/observability platform
- API gateway
- Feature flag platform
- Payment processing
- Inventory/reservation system
- News feed
- Chat/messaging system
- Ride matching
- Ad/event tracking
- CDC pipeline
- Workflow orchestration

For each topic, use the same structure:

Requirements
→ Scale
→ APIs
→ Data model
→ Architecture
→ Deep dive
→ Bottlenecks
→ Failure modes
→ Trade-offs
→ Observability
→ Security
→ Multi-region
→ Evolution
→ Interview questions

---

# Interview Answer Style

The user prefers answers that are:

- Practical
- Precise
- Interview-ready
- Technically deep
- Structured
- Not generic
- Grounded in real production scenarios

For system design, avoid simply listing technologies.

Explain:

Why this component?

What problem does it solve?

What happens at scale?

What happens when it fails?

What trade-off does it introduce?

How would you evolve it?

---

# Important Continuation Context

If another ChatGPT session loads this file, assume:

- The user is continuing Staff/Senior Staff interview preparation.
- The GitHub repository is the central study repository.
- The Rate Limiter design is the first completed deep dive.
- New system-design topics should be added consistently with the existing Rate Limiter structure.
- Java is the default implementation language.
- The material should be useful for a 45–60 minute Staff-level system-design interview.
- The goal is not merely to produce documentation; the goal is to help the user explain designs clearly under interview pressure.

When creating new designs, also consider adding:

- A polished HTML deep-dive page
- Java examples where appropriate
- A link from index.html
- A short set of Staff-level interview questions
- A concise model-answer / closing summary
- Failure-mode and trade-off sections
- Production scaling considerations
